package com.pommala.arka.whatsapp.service.internal;

import com.pommala.arka.whatsapp.model.ResolvedWhatsAppFlow;
import com.pommala.arka.whatsapp.support.code.WhatsAppProviderCode;
import com.pommala.arka.whatsapp.support.code.WhatsAppConfigCategory;
import com.pommala.arka.whatsapp.support.exception.UnknownWhatsAppFlowException;
import com.pommala.arka.whatsapp.support.exception.WhatsAppConfigurationException;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * YAML-backed WhatsApp flow provider.
 *
 * <p>Resolves flow definitions from {@code arka.flows.*} and sender definitions
 * from {@code arka.senders.*} in the application YAML configuration.
 */
public class YamlWhatsAppFlowProvider {

    private static final String LOG_PREFIX = "[arka-whatsapp-provider-yaml]";
    private static final Logger log = LoggerFactory.getLogger(YamlWhatsAppFlowProvider.class);

    private final YamlFlowProperties props;

    public YamlWhatsAppFlowProvider(YamlFlowProperties props) {
        this.props = props;
    }

    public ResolvedWhatsAppFlow resolve(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        if (rawFlow == null) {
            throw new UnknownWhatsAppFlowException(WhatsAppProviderCode.FLOW_NOT_FOUND,
                    "WhatsApp flow not found: " + flowKey);
        }
        return mapFlow(flowKey, rawFlow);
    }

    public Optional<ResolvedWhatsAppFlow> find(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        return rawFlow == null ? Optional.empty() : Optional.of(mapFlow(flowKey, rawFlow));
    }

    public java.util.Set<String> flowKeys() {
        return props.getFlows().keySet();
    }

    private ResolvedWhatsAppFlow mapFlow(String flowKey, Map<String, Object> raw) {
        var deliveryMode = getString(raw, "delivery-mode");
        var senderRef    = getString(raw, "sender-ref");
        var templateName = getString(raw, "template-name");
        var languageCode = getString(raw, "language-code");
        var defaultBody  = getString(raw, "default-body");

        if (deliveryMode == null || deliveryMode.isBlank()) {
            throw new WhatsAppConfigurationException(
                    WhatsAppProviderCode.FLOW_NOT_FOUND, WhatsAppConfigCategory.DELIVERY_MODE,
                    "Flow '" + flowKey + "' is missing delivery-mode");
        }

        var sender = resolveSender(flowKey, senderRef);
        var phoneNumberId = getString(sender, "phone-number-id");
        var accessToken   = getString(sender, "access-token");

        if (phoneNumberId == null || phoneNumberId.isBlank()) {
            throw new WhatsAppConfigurationException(
                    WhatsAppProviderCode.FLOW_NOT_FOUND, WhatsAppConfigCategory.SENDER,
                    "Sender for flow '" + flowKey + "' missing phone-number-id");
        }

        return new ResolvedWhatsAppFlow(flowKey, deliveryMode,
                phoneNumberId, accessToken != null ? accessToken : "",
                templateName, languageCode, defaultBody, Map.of());
    }

    private Map<String, Object> resolveSender(String flowKey, String senderRef) {
        if (senderRef == null || senderRef.isBlank()) {
            throw new WhatsAppConfigurationException(
                    WhatsAppProviderCode.FLOW_NOT_FOUND, WhatsAppConfigCategory.SENDER,
                    "Flow '" + flowKey + "' is missing sender-ref");
        }
        var rawSender = props.getSenders().get(senderRef);
        if (rawSender == null) {
            throw new WhatsAppConfigurationException(
                    WhatsAppProviderCode.FLOW_NOT_FOUND, WhatsAppConfigCategory.SENDER,
                    "Sender reference '" + senderRef + "' could not be resolved for flow '" + flowKey + "'");
        }
        return rawSender;
    }

    private static String getString(Map<String, Object> map, String key) {
        var v = map.get(key);
        return v instanceof String s ? s : null;
    }
}
