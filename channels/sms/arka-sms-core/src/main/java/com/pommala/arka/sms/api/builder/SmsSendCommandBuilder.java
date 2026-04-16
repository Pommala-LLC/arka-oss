package com.pommala.arka.sms.api.builder;

import com.pommala.arka.sms.api.SmsSendCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Fluent builder for {@link SmsSendCommand}.
 */
public final class SmsSendCommandBuilder {

    private final String flowKey;
    private String to;
    private final Map<String, Object> model = new HashMap<>();
    private String bodyOverride;
    private String fromOverride;

    public SmsSendCommandBuilder(String flowKey) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        this.flowKey = flowKey;
    }

    public SmsSendCommandBuilder to(String phoneNumber) { this.to = phoneNumber; return this; }
    public SmsSendCommandBuilder model(String key, Object value) { model.put(key, value); return this; }
    public SmsSendCommandBuilder model(Map<String, Object> variables) { model.putAll(variables); return this; }
    public SmsSendCommandBuilder bodyOverride(String body) { this.bodyOverride = body; return this; }
    public SmsSendCommandBuilder fromOverride(String from) { this.fromOverride = from; return this; }

    public SmsSendCommand build() {
        return new SmsSendCommand(flowKey, to, Map.copyOf(model), bodyOverride, fromOverride);
    }
}
