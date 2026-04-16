package com.pommala.arka.teams.web.internal;
import java.util.List;
import java.util.Map;
public record TeamsBatchSendRequest(String flowKey, List<String> channelIds, Map<String, Object> model) {
    public TeamsBatchSendRequest { channelIds = channelIds != null ? List.copyOf(channelIds) : List.of(); model = model != null ? Map.copyOf(model) : Map.of(); }
}
