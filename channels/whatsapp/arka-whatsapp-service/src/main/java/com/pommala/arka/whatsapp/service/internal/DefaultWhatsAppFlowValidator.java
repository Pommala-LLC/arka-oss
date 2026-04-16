package com.pommala.arka.whatsapp.service.internal;

import com.pommala.arka.whatsapp.spi.WhatsAppFlowValidator;
import com.pommala.arka.whatsapp.support.code.WhatsAppValidationCode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default WhatsApp-specific flow validator.
 *
 * <p>Validates all WhatsApp flow configurations at startup.
 * Business rule: startup validation is fail-fast with full collection.
 */
public class DefaultWhatsAppFlowValidator implements WhatsAppFlowValidator {

    private static final String LOG_PREFIX = "[arka-whatsapp-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultWhatsAppFlowValidator.class);

    private final YamlWhatsAppFlowProvider flowProvider;

    public DefaultWhatsAppFlowValidator(YamlWhatsAppFlowProvider flowProvider) {
        this.flowProvider = flowProvider;
    }

    @Override
    public String channelId() { return "whatsapp"; }

    @Override
    public List<String> validate() {
        var errors = new ArrayList<String>();
        var flowKeys = flowProvider.flowKeys();

        log.info("{} Validating {} WhatsApp flow(s)", LOG_PREFIX, flowKeys.size());

        for (var flowKey : flowKeys) {
            try {
                var flow = flowProvider.resolve(flowKey);
                if (flow.phoneNumberId() == null || flow.phoneNumberId().isBlank()) {
                    errors.add("[" + flowKey + "] " + WhatsAppValidationCode.PHONE_NUMBER_ID_MISSING.defaultMessage());
                }
                if (flow.isTemplateMode()) {
                    if (flow.templateName() == null || flow.templateName().isBlank()) {
                        errors.add("[" + flowKey + "] " + WhatsAppValidationCode.META_TEMPLATE_NAME_MISSING.defaultMessage());
                    }
                    if (flow.languageCode() == null || flow.languageCode().isBlank()) {
                        errors.add("[" + flowKey + "] " + WhatsAppValidationCode.LANGUAGE_CODE_MISSING.defaultMessage());
                    }
                }
            } catch (Exception ex) {
                errors.add("[" + flowKey + "] " + ex.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            log.error("{} {} WhatsApp flow validation error(s) found", LOG_PREFIX, errors.size());
        }
        return errors;
    }
}
