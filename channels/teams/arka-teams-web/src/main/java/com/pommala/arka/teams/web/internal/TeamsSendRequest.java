package com.pommala.arka.teams.web.internal;
import java.util.Map;
public record TeamsSendRequest(String flowKey, String channelId, Map<String, Object> model, String textOverride, String replyToId) {
    public TeamsSendRequest { model = model != null ? Map.copyOf(model) : Map.of(); }
}
