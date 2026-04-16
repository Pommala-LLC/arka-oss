package com.pommala.arka.sms.service.internal;

import com.pommala.arka.sms.model.ResolvedSmsFlow;
import com.pommala.arka.sms.support.code.SmsCoreCode;
import com.pommala.arka.sms.support.exception.UnknownSmsFlowException;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import java.util.Map;
import java.util.Optional;

public class YamlSmsFlowProvider {

    private final YamlFlowProperties props;

    public YamlSmsFlowProvider(YamlFlowProperties props) { this.props = props; }

    public ResolvedSmsFlow resolve(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        if (rawFlow == null) {
            throw new UnknownSmsFlowException(SmsCoreCode.SEND_FAILED, "SMS flow not found: " + flowKey);
        }
        return mapFlow(flowKey, rawFlow);
    }

    public Optional<ResolvedSmsFlow> find(String flowKey) {
        var rawFlow = props.getFlows().get(flowKey);
        return rawFlow == null ? Optional.empty() : Optional.of(mapFlow(flowKey, rawFlow));
    }

    public java.util.Set<String> flowKeys() { return props.getFlows().keySet(); }

    private ResolvedSmsFlow mapFlow(String flowKey, Map<String, Object> raw) {
        var template    = getString(raw, "template");
        var senderRef   = getString(raw, "sender-ref");
        var messageType = getString(raw, "message-type");
        var sender = resolveSender(flowKey, senderRef);
        var fromNumber = getString(sender, "from");
        return new ResolvedSmsFlow(flowKey,
                template != null ? template : "",
                fromNumber != null ? fromNumber : "",
                messageType);
    }

    private Map<String, Object> resolveSender(String flowKey, String senderRef) {
        if (senderRef == null || senderRef.isBlank()) return Map.of();
        var rawSender = props.getSenders().get(senderRef);
        if (rawSender == null) return Map.of();
        return rawSender;
    }

    private static String getString(Map<String, Object> map, String key) {
        var v = map.get(key);
        return v instanceof String s ? s : null;
    }
}
