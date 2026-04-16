package com.pommala.arka.slack.service.internal;

import com.pommala.arka.slack.model.ResolvedSlackFlow;
import com.pommala.arka.slack.support.code.SlackCoreCode;
import com.pommala.arka.slack.support.exception.UnknownSlackFlowException;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import java.util.Map;
import java.util.Optional;

public class YamlSlackFlowProvider {

    private final YamlFlowProperties props;
    public YamlSlackFlowProvider(YamlFlowProperties props) { this.props = props; }

    public ResolvedSlackFlow resolve(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        if (rawFlow == null) throw new UnknownSlackFlowException(SlackCoreCode.SEND_FAILED, "Slack flow not found: " + flowKey);
        return mapFlow(flowKey, rawFlow);
    }

    public Optional<ResolvedSlackFlow> find(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        return rawFlow == null ? Optional.empty() : Optional.of(mapFlow(flowKey, rawFlow));
    }

    public java.util.Set<String> flowKeys() { return props.getFlows().keySet(); }

    private ResolvedSlackFlow mapFlow(String flowKey, Map<String, Object> raw) {
        var template       = getString(raw, "template");
        var transportMode  = getString(raw, "transport-mode");
        var senderRef      = getString(raw, "sender-ref");
        var defaultChannel = getString(raw, "default-channel");
        var messageFormat  = getString(raw, "message-format");

        String webhookUrl = null, botToken = null;
        if (senderRef != null && !senderRef.isBlank()) {
            var sender = props.getSenders().get(senderRef);
            if (sender != null) {
                webhookUrl = getString(sender, "webhook-url");
                botToken   = getString(sender, "bot-token");
            }
        }
        return new ResolvedSlackFlow(flowKey, template,
                transportMode != null ? transportMode : "webhook",
                webhookUrl, botToken, defaultChannel, messageFormat);
    }

    private static String getString(Map<String, Object> map, String key) {
        var v = map.get(key); return v instanceof String s ? s : null;
    }
}
