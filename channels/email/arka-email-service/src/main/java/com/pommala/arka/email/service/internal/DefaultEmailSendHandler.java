package com.pommala.arka.email.service.internal;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.email.spi.EmailMessageBuilder;
import com.pommala.arka.email.spi.EmailTransport;
import com.pommala.arka.email.spi.RecipientPolicyHandler;
import com.pommala.arka.email.support.code.EmailRecipientCode;
import com.pommala.arka.email.support.code.EmailTransportCode;
import com.pommala.arka.email.support.exception.EmailTransportException;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.BusinessRuleViolationException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Default email channel send pipeline.
 *
 * <p>Implements the full email send sequence as a {@link ChannelSendHandler}:
 * <ol>
 *   <li>Flow resolution via {@link YamlEmailFlowProvider}.</li>
 *   <li>Recipient normalization via {@link RecipientPolicyHandler}.</li>
 *   <li>Empty-to guard.</li>
 *   <li>Message construction via {@link EmailMessageBuilder}.</li>
 *   <li>Transport send via {@link EmailTransport} (with retry if policy present).</li>
 * </ol>
 *
 * <p>Interceptors are called by the shared {@code DefaultMessageFlowService} —
 * this handler must not call them.
 */
public class DefaultEmailSendHandler implements ChannelSendHandler {

    /** Channel identifier this handler serves. */
    public static final String CHANNEL_ID = "email";

    private static final String LOG_PREFIX = "[arka-email-service]";

    /**
     * Sentinel value for missing tracking context.
     * Must trigger a WARN log when encountered — never silently accepted.
     * Never a fallback UUID.
     */
    private static final String UNTRACED = "untraced";

    private static final Logger log = LoggerFactory.getLogger(DefaultEmailSendHandler.class);

    private final YamlEmailFlowProvider flowProvider;
    private final RecipientPolicyHandler recipientHandler;
    private final EmailMessageBuilder messageBuilder;
    private final EmailTransport transport;
    private final Optional<RetryPolicy> retryPolicy;

    public DefaultEmailSendHandler(YamlEmailFlowProvider flowProvider,
                                   RecipientPolicyHandler recipientHandler,
                                   EmailMessageBuilder messageBuilder,
                                   EmailTransport transport,
                                   Optional<RetryPolicy> retryPolicy) {
        this.flowProvider     = flowProvider;
        this.recipientHandler = recipientHandler;
        this.messageBuilder   = messageBuilder;
        this.transport        = transport;
        this.retryPolicy      = retryPolicy;
    }

    @Override
    public String channelId() { return CHANNEL_ID; }

    @Override
    public DeliveryResult handle(SendCommand command) {
        // Cast to email command — type safety at the channel boundary.
        if (!(command instanceof EmailSendCommand emailCommand)) {
            throw new IllegalArgumentException(
                    "DefaultEmailSendHandler received non-email command: " +
                    command.getClass().getSimpleName());
        }

        // Tracking ID rule: downstream modules read from MDC — never generate.
        // Missing context → "untraced" + WARN log. Never a fallback UUID.
        var rawCorrelationId = MDC.get("correlationId");
        if (rawCorrelationId == null || rawCorrelationId.isBlank()) {
            log.warn("{} Missing correlationId in MDC — using untraced [flowKey={}]",
                    LOG_PREFIX, emailCommand.flowKey());
        }
        final var correlationId = (rawCorrelationId != null && !rawCorrelationId.isBlank())
                ? rawCorrelationId : UNTRACED;

        MDC.put("correlationId", correlationId);
        log.info("{} Handling email send [flowKey={}, correlationId={}]",
                LOG_PREFIX, emailCommand.flowKey(), correlationId);

        try {
            return executeEmailPipeline(emailCommand, correlationId);
        } finally {
            MDC.remove("correlationId");
        }
    }

    private DeliveryResult executeEmailPipeline(EmailSendCommand command, String correlationId) {
        // Step 1: resolve flow
        var flow = flowProvider.resolve(command.flowKey());

        // Step 2: normalize recipients
        var recipients = recipientHandler.normalize(
                command.to(), command.cc(), command.bcc());

        // Step 3: empty-to guard
        // Business rule: at least one valid primary recipient required.
        // This guard belongs in the service layer, not in the normalizer.
        if (!recipients.hasPrimaryRecipients()) {
            throw new BusinessRuleViolationException(
                    EmailRecipientCode.NO_VALID_PRIMARY_RECIPIENT,
                    "No valid primary recipient after normalization [flowKey=" + command.flowKey() + "]");
        }

        // Step 4: build message
        var message = messageBuilder.build(flow, command, recipients);

        // Step 5: send with retry if policy present
        sendWithOptionalRetry(message);

        log.info("{} Email sent [flowKey={}, correlationId={}]",
                LOG_PREFIX, command.flowKey(), correlationId);
        return DeliveryResult.success(command.flowKey());
    }

    private void sendWithOptionalRetry(com.pommala.arka.email.model.FinalEmailMessage message) {
        if (retryPolicy.isEmpty()) {
            // OSS: single attempt — no retry loop
            transport.send(message);
            return;
        }

        var policy = retryPolicy.get();
        var maxAttempts = policy.maxAttempts();
        ArkaApplicationException lastEx = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                transport.send(message);
                return; // success
            } catch (ArkaApplicationException ex) {
                lastEx = ex;
                // Retry classification: non-retriable failures fail immediately.
                if (!policy.isRetriable(ex)) {
                    log.warn("{} Non-retriable transport failure [attempt={}, code={}]",
                            LOG_PREFIX, attempt, ex.internalCode().code());
                    throw ex;
                }
                if (attempt < maxAttempts) {
                    log.warn("{} Retriable transport failure [attempt={}/{}, code={}] — retrying after {}ms",
                            LOG_PREFIX, attempt, maxAttempts, ex.internalCode().code(),
                            policy.delay().toMillis());
                    try {
                        Thread.sleep(policy.delay().toMillis());
                    } catch (InterruptedException ie) {
                        // InterruptedException rule: always restore interrupt flag.
                        Thread.currentThread().interrupt();
                        throw new EmailTransportException(
                                EmailTransportCode.SMTP_SEND_FAILED,
                                "Transport interrupted during retry delay", ie);
                    }
                }
            }
        }
        // All attempts exhausted
        throw lastEx;
    }
}
