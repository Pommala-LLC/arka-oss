package com.pommala.arka.sms.service.internal;

import com.pommala.arka.sms.spi.SmsFlowValidator;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSmsFlowValidator implements SmsFlowValidator {

    private static final String LOG_PREFIX = "[arka-sms-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultSmsFlowValidator.class);
    private final YamlSmsFlowProvider flowProvider;

    public DefaultSmsFlowValidator(YamlSmsFlowProvider flowProvider) { this.flowProvider = flowProvider; }

    @Override public String channelId() { return "sms"; }

    @Override
    public List<String> validate() {
        var errors = new ArrayList<String>();
        var flowKeys = flowProvider.flowKeys();
        log.info("{} Validating {} SMS flow(s)", LOG_PREFIX, flowKeys.size());
        for (var flowKey : flowKeys) {
            try {
                var flow = flowProvider.resolve(flowKey);
                if (flow.fromNumber() == null || flow.fromNumber().isBlank()) {
                    errors.add("[" + flowKey + "] Sender from-number is missing");
                }
                if (flow.template() == null || flow.template().isBlank()) {
                    errors.add("[" + flowKey + "] Template is missing");
                }
            } catch (Exception ex) { errors.add("[" + flowKey + "] " + ex.getMessage()); }
        }
        if (!errors.isEmpty()) { log.error("{} {} SMS flow validation error(s)", LOG_PREFIX, errors.size()); }
        return errors;
    }
}
