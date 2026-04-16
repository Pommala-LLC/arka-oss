package com.pommala.arka.email.service.internal;

import com.pommala.arka.email.spi.EmailFlowValidator;
import com.pommala.arka.email.support.code.EmailValidationCode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default email-specific flow validator.
 *
 * <p>Validates all email flow configurations at application startup.
 * Contributes to the shared validation engine via {@link EmailFlowValidator}.
 *
 * <p>Business rule: startup validation is fail-fast with full collection.
 * All errors collected before returning — operator sees the complete picture.
 */
public class DefaultEmailFlowValidator implements EmailFlowValidator {

    private static final String LOG_PREFIX = "[arka-email-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultEmailFlowValidator.class);

    private final YamlEmailFlowProvider flowProvider;

    public DefaultEmailFlowValidator(YamlEmailFlowProvider flowProvider) {
        this.flowProvider = flowProvider;
    }

    @Override
    public String channelId() { return "email"; }

    @Override
    public List<String> validate() {
        // Business rule: collect ALL errors before returning.
        var errors = new ArrayList<String>();
        var flowKeys = flowProvider.flowKeys();

        log.info("{} Validating {} email flow(s)", LOG_PREFIX, flowKeys.size());

        for (var flowKey : flowKeys) {
            try {
                var flow = flowProvider.resolve(flowKey);
                validateFlow(flowKey, flow, errors);
            } catch (Exception ex) {
                errors.add("[" + flowKey + "] " + ex.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            log.error("{} {} email flow validation error(s) found", LOG_PREFIX, errors.size());
        }
        return errors;
    }

    private void validateFlow(String flowKey, com.pommala.arka.email.model.ResolvedEmailFlow flow,
                              List<String> errors) {
        // Business rule: template is always required — cannot be suppressed.
        if (flow.template() == null || flow.template().isBlank()) {
            errors.add("[" + flowKey + "] " + EmailValidationCode.TEMPLATE_MISSING.defaultMessage());
        }
        // Business rule: subject is always required — cannot be suppressed.
        if (flow.subject() == null || flow.subject().isBlank()) {
            errors.add("[" + flowKey + "] " + EmailValidationCode.SUBJECT_MISSING.defaultMessage());
        }
        // Business rule: sender from address is always required.
        if (flow.sender() == null || flow.sender().from().isBlank()) {
            errors.add("[" + flowKey + "] " + EmailValidationCode.SENDER_FROM_INVALID.defaultMessage());
        }
    }
}
