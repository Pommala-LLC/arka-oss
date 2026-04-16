package com.pommala.arka.slack.api.builder;

import com.pommala.arka.slack.api.SlackSendCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SlackSendCommandBuilder {

    private final String flowKey;
    private String channelId;
    private final Map<String, Object> model = new HashMap<>();
    private String textOverride;
    private String threadTs;

    public SlackSendCommandBuilder(String flowKey) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        this.flowKey = flowKey;
    }

    public SlackSendCommandBuilder channelId(String channelId) { this.channelId = channelId; return this; }
    public SlackSendCommandBuilder model(String key, Object value) { model.put(key, value); return this; }
    public SlackSendCommandBuilder model(Map<String, Object> variables) { model.putAll(variables); return this; }
    public SlackSendCommandBuilder textOverride(String text) { this.textOverride = text; return this; }
    public SlackSendCommandBuilder threadTs(String ts) { this.threadTs = ts; return this; }

    public SlackSendCommand build() {
        return new SlackSendCommand(flowKey, channelId, Map.copyOf(model), textOverride, threadTs);
    }
}
