package com.pommala.arka.slack.web.internal;
import java.util.Map;
public record SlackSendRequest(String flowKey, String channelId, Map<String, Object> model, String textOverride, String threadTs) {
    public SlackSendRequest { model = model != null ? Map.copyOf(model) : Map.of(); }
}
