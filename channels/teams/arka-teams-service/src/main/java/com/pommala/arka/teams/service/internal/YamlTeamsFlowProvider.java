package com.pommala.arka.teams.service.internal;

import com.pommala.arka.teams.model.ResolvedTeamsFlow;
import com.pommala.arka.teams.support.code.TeamsCoreCode;
import com.pommala.arka.teams.support.exception.UnknownTeamsFlowException;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import java.util.Map;
import java.util.Optional;

public class YamlTeamsFlowProvider {
    private final YamlFlowProperties props;
    public YamlTeamsFlowProvider(YamlFlowProperties props) { this.props = props; }

    public ResolvedTeamsFlow resolve(String flowKey) {
        var raw = props.getFlows().get(flowKey);
        if (raw == null) throw new UnknownTeamsFlowException(TeamsCoreCode.SEND_FAILED, "Teams flow not found: " + flowKey);
        return mapFlow(flowKey, raw);
    }
    public Optional<ResolvedTeamsFlow> find(String flowKey) {
        var raw = props.getFlows().get(flowKey); return raw == null ? Optional.empty() : Optional.of(mapFlow(flowKey, raw));
    }
    public java.util.Set<String> flowKeys() { return props.getFlows().keySet(); }

    private ResolvedTeamsFlow mapFlow(String flowKey, Map<String, Object> raw) {
        var template       = getString(raw, "template");
        var transportMode  = getString(raw, "transport-mode");
        var senderRef      = getString(raw, "sender-ref");
        var defaultChannel = getString(raw, "default-channel");
        var messageFormat  = getString(raw, "message-format");
        String webhookUrl = null, tenantId = null, clientId = null, clientSecret = null;
        if (senderRef != null && !senderRef.isBlank()) {
            var sender = props.getSenders().get(senderRef);
            if (sender != null) {
                webhookUrl   = getString(sender, "webhook-url");
                tenantId     = getString(sender, "tenant-id");
                clientId     = getString(sender, "client-id");
                clientSecret = getString(sender, "client-secret");
            }
        }
        return new ResolvedTeamsFlow(flowKey, template, transportMode != null ? transportMode : "webhook",
                webhookUrl, tenantId, clientId, clientSecret, defaultChannel, messageFormat);
    }
    private static String getString(Map<String, Object> map, String key) { var v = map.get(key); return v instanceof String s ? s : null; }
}
