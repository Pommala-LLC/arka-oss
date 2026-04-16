package com.pommala.arka.email.service.internal;

import com.pommala.arka.email.model.ResolvedEmailFlow;
import com.pommala.arka.email.model.ResolvedSender;
import com.pommala.arka.email.support.code.EmailConfigCategory;
import com.pommala.arka.email.support.code.EmailProviderCode;
import com.pommala.arka.email.support.exception.UnknownEmailFlowException;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pommala.arka.email.support.exception.EmailConfigurationException;

/**
 * YAML-backed email flow provider.
 *
 * <p>Resolves flow definitions from {@code arka.flows.*} and sender definitions
 * from {@code arka.senders.*} in the application YAML configuration.
 *
 * <p>YAML safe construction is handled by {@link com.pommala.arka.provider.yaml.internal.YamlFlowLoader}.
 */
public class YamlEmailFlowProvider {

    private static final String LOG_PREFIX = "[arka-email-provider-yaml]";
    private static final Logger log = LoggerFactory.getLogger(YamlEmailFlowProvider.class);

    private final YamlFlowProperties props;

    public YamlEmailFlowProvider(YamlFlowProperties props) {
        this.props = props;
    }

    /**
     * Resolves the flow for the given key.
     *
     * @param flowKey the flow identifier, never null
     * @return the resolved flow
     * @throws UnknownEmailFlowException if not found
     */
    public ResolvedEmailFlow resolve(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        if (rawFlow == null) {
            throw new UnknownEmailFlowException(EmailProviderCode.FLOW_NOT_FOUND_ANYWHERE,
                    "Email flow not found: " + flowKey);
        }
        return mapFlow(flowKey, rawFlow);
    }

    /**
     * Returns whether a flow exists for the given key.
     *
     * @param flowKey the flow identifier
     * @return true if a flow definition exists
     */
    public Optional<ResolvedEmailFlow> find(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        return rawFlow == null ? Optional.empty() : Optional.of(mapFlow(flowKey, rawFlow));
    }

    /** Returns all flow keys defined in YAML configuration. */
    public java.util.Set<String> flowKeys() {
        return props.getFlows().keySet();
    }

    private ResolvedEmailFlow mapFlow(String flowKey, Map<String, Object> raw) {
        var template  = getString(raw, "template");
        var subject   = getString(raw, "subject");
        var senderRef = getString(raw, "sender-ref");

        var sender = resolveSender(flowKey, senderRef);

        var staticCc  = getStringList(raw, "cc");
        var staticBcc = getStringList(raw, "bcc");

        return new ResolvedEmailFlow(flowKey, template, subject, sender,
                staticCc, staticBcc, Map.of());
    }

    private ResolvedSender resolveSender(String flowKey, String senderRef) {
        if (senderRef == null || senderRef.isBlank()) {
            throw new EmailConfigurationException(
                    EmailProviderCode.SENDER_REF_MISSING,
                    EmailConfigCategory.SENDER,
                    "Flow '" + flowKey + "' is missing sender-ref");
        }
        var rawSender = props.getSenders().get(senderRef);
        if (rawSender == null) {
            throw new EmailConfigurationException(
                    EmailProviderCode.SENDER_REF_UNRESOLVABLE,
                    EmailConfigCategory.SENDER,
                    "Sender reference '" + senderRef + "' could not be resolved for flow '" + flowKey + "'");
        }
        var from        = getString(rawSender, "from");
        var displayName = getString(rawSender, "display-name");
        var replyTo     = getString(rawSender, "reply-to");
        return new ResolvedSender(from != null ? from : "", displayName, replyTo);
    }

    private static String getString(Map<String, Object> map, String key) {
        var v = map.get(key);
        return v instanceof String s ? s : null;
    }

    @SuppressWarnings("unchecked")
    private static java.util.List<String> getStringList(Map<String, Object> map, String key) {
        var v = map.get(key);
        if (v instanceof java.util.List<?> list) {
            return list.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }
        return java.util.List.of();
    }
}
