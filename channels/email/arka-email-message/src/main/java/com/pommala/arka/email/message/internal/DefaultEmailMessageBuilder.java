package com.pommala.arka.email.message.internal;

import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.email.model.FinalEmailMessage;
import com.pommala.arka.email.model.NormalizedRecipients;
import com.pommala.arka.email.model.ResolvedEmailFlow;
import com.pommala.arka.email.spi.EmailMessageBuilder;
import com.pommala.arka.email.support.code.EmailMessageCode;
import com.pommala.arka.email.support.code.EmailTemplateCode;
import com.pommala.arka.email.support.code.EmailConfigCategory;
import com.pommala.arka.email.support.exception.EmailConfigurationException;
import com.pommala.arka.spi.TemplateRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Default email message construction pipeline.
 *
 * <p>Renders the template (if a renderer is present), applies sender identity,
 * merges static and runtime CC/BCC, injects Reply-To, and binds attachments.
 * Produces an immutable {@link FinalEmailMessage}.
 *
 * <h3>Tracking ID rule:</h3>
 * correlationId is read from MDC — never generated here.
 */
public class DefaultEmailMessageBuilder implements EmailMessageBuilder {

    private static final String LOG_PREFIX = "[arka-email-message]";
    private static final String UNTRACED   = "untraced";
    private static final Logger log = LoggerFactory.getLogger(DefaultEmailMessageBuilder.class);

    private final Optional<TemplateRenderer> renderer;

    public DefaultEmailMessageBuilder(Optional<TemplateRenderer> renderer) {
        this.renderer = Objects.requireNonNull(renderer, "renderer must not be null");
    }

    @Override
    public FinalEmailMessage build(ResolvedEmailFlow flow,
                                   EmailSendCommand command,
                                   NormalizedRecipients recipients) {
        Objects.requireNonNull(flow,       "flow must not be null");
        Objects.requireNonNull(command,    "command must not be null");
        Objects.requireNonNull(recipients, "recipients must not be null");

        // Tracking ID rule: read from MDC — never generate.
        var rawId = MDC.get("correlationId");
        final var correlationId = (rawId != null && !rawId.isBlank()) ? rawId : UNTRACED;

        // Render template or use fallback body
        var htmlBody = renderBody(flow, command);

        // Determine subject: command override takes precedence over flow subject
        var subject = (command.subjectOverride() != null && !command.subjectOverride().isBlank())
                ? command.subjectOverride() : flow.subject();

        // Merge static flow CC/BCC with runtime command CC/BCC
        var mergedCc  = merge(flow.staticCc(),  recipients.cc());
        var mergedBcc = merge(flow.staticBcc(), recipients.bcc());

        // Apply sender display name and resolve reply-to
        var sender  = flow.sender();
        var from    = sender.formatted();
        var replyTo = sender.hasReplyTo() ? sender.replyTo() : null;

        try {
            return FinalEmailMessage.builder()
                    .flowKey(flow.flowKey())
                    .from(from)
                    .replyTo(replyTo)
                    .to(recipients.to())
                    .cc(mergedCc)
                    .bcc(mergedBcc)
                    .subject(subject)
                    .htmlBody(htmlBody)
                    .attachments(flow.attachmentPaths())
                    .correlationId(correlationId)
                    .build();
        } catch (Exception ex) {
            throw new EmailConfigurationException(
                    EmailMessageCode.MESSAGE_BUILD_FAILED,
                    EmailConfigCategory.MESSAGE,
                    "Failed to build email message for flow '" + flow.flowKey() + "'", ex);
        }
    }

    private String renderBody(ResolvedEmailFlow flow, EmailSendCommand command) {
        if (renderer.isPresent()) {
            try {
                return renderer.get().render(flow.template(), command.model());
            } catch (Exception ex) {
                throw new EmailConfigurationException(
                        EmailTemplateCode.TEMPLATE_RENDER_FAILED,
                        EmailConfigCategory.TEMPLATE,
                        "Template rendering failed for '" + flow.template() + "'", ex);
            }
        }
        // No template renderer — use empty body; consumer must supply pre-rendered content
        log.warn("{} No TemplateRenderer present — producing empty body [flowKey={}]",
                LOG_PREFIX, flow.flowKey());
        return "";
    }

    private static List<String> merge(List<String> staticList, List<String> runtimeList) {
        var merged = new ArrayList<String>(staticList.size() + runtimeList.size());
        merged.addAll(staticList);
        merged.addAll(runtimeList);
        return merged;
    }
}
