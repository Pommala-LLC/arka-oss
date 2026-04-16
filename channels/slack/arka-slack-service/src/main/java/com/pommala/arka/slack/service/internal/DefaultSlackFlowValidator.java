package com.pommala.arka.slack.service.internal;

import com.pommala.arka.slack.spi.SlackFlowValidator;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSlackFlowValidator implements SlackFlowValidator {

    private static final String LOG_PREFIX = "[arka-slack-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultSlackFlowValidator.class);
    private final YamlSlackFlowProvider flowProvider;

    public DefaultSlackFlowValidator(YamlSlackFlowProvider flowProvider) { this.flowProvider = flowProvider; }
    @Override public String channelId() { return "slack"; }

    @Override
    public List<String> validate() {
        var errors = new ArrayList<String>();
        var flowKeys = flowProvider.flowKeys();
        log.info("{} Validating {} Slack flow(s)", LOG_PREFIX, flowKeys.size());
        for (var flowKey : flowKeys) {
            try {
                var flow = flowProvider.resolve(flowKey);
                if (flow.isWebhookMode() && (flow.webhookUrl() == null || flow.webhookUrl().isBlank()))
                    errors.add("[" + flowKey + "] Webhook URL is missing for webhook transport mode");
                if (flow.isApiMode() && (flow.botToken() == null || flow.botToken().isBlank()))
                    errors.add("[" + flowKey + "] Bot token is missing for API transport mode");
            } catch (Exception ex) { errors.add("[" + flowKey + "] " + ex.getMessage()); }
        }
        if (!errors.isEmpty()) log.error("{} {} Slack flow validation error(s)", LOG_PREFIX, errors.size());
        return errors;
    }
}
