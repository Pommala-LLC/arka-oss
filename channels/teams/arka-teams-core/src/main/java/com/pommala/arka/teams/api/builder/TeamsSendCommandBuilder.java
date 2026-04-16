package com.pommala.arka.teams.api.builder;

import com.pommala.arka.teams.api.TeamsSendCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class TeamsSendCommandBuilder {

    private final String flowKey;
    private String channelId;
    private final Map<String, Object> model = new HashMap<>();
    private String textOverride;
    private String replyToId;

    public TeamsSendCommandBuilder(String flowKey) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        this.flowKey = flowKey;
    }

    public TeamsSendCommandBuilder channelId(String channelId) { this.channelId = channelId; return this; }
    public TeamsSendCommandBuilder model(String key, Object value) { model.put(key, value); return this; }
    public TeamsSendCommandBuilder model(Map<String, Object> variables) { model.putAll(variables); return this; }
    public TeamsSendCommandBuilder textOverride(String text) { this.textOverride = text; return this; }
    public TeamsSendCommandBuilder replyToId(String id) { this.replyToId = id; return this; }

    public TeamsSendCommand build() {
        return new TeamsSendCommand(flowKey, channelId, Map.copyOf(model), textOverride, replyToId);
    }
}
