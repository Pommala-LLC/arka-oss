package com.pommala.arka.teams.service.internal;

import com.pommala.arka.teams.spi.TeamsFlowValidator;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTeamsFlowValidator implements TeamsFlowValidator {
    private static final String LOG_PREFIX = "[arka-teams-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultTeamsFlowValidator.class);
    private final YamlTeamsFlowProvider flowProvider;
    public DefaultTeamsFlowValidator(YamlTeamsFlowProvider flowProvider) { this.flowProvider = flowProvider; }
    @Override public String channelId() { return "teams"; }

    @Override
    public List<String> validate() {
        var errors = new ArrayList<String>();
        log.info("{} Validating {} Teams flow(s)", LOG_PREFIX, flowProvider.flowKeys().size());
        for (var flowKey : flowProvider.flowKeys()) {
            try {
                var flow = flowProvider.resolve(flowKey);
                if (flow.isWebhookMode() && (flow.webhookUrl() == null || flow.webhookUrl().isBlank()))
                    errors.add("[" + flowKey + "] Webhook URL missing for webhook mode");
                if (flow.isGraphMode()) {
                    if (flow.tenantId() == null || flow.tenantId().isBlank()) errors.add("[" + flowKey + "] Tenant ID missing for graph mode");
                    if (flow.clientId() == null || flow.clientId().isBlank()) errors.add("[" + flowKey + "] Client ID missing for graph mode");
                }
            } catch (Exception ex) { errors.add("[" + flowKey + "] " + ex.getMessage()); }
        }
        if (!errors.isEmpty()) log.error("{} {} Teams flow validation error(s)", LOG_PREFIX, errors.size());
        return errors;
    }
}
