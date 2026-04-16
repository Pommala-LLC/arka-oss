package com.pommala.arka.slack.web.internal;
import java.util.List;
import java.util.Map;
public record SlackBatchSendRequest(String flowKey, List<String> channelIds, Map<String, Object> model) {
    public SlackBatchSendRequest { channelIds = channelIds != null ? List.copyOf(channelIds) : List.of(); model = model != null ? Map.copyOf(model) : Map.of(); }
}
