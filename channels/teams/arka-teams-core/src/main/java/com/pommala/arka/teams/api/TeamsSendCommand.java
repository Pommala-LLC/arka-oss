package com.pommala.arka.teams.api;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.teams.api.builder.TeamsSendCommandBuilder;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable Teams send command.
 *
 * @param flowKey        the flow key identifying the delivery configuration
 * @param channelId      target Teams channel or chat ID
 * @param model          template variables, never null, may be empty
 * @param textOverride   optional text override; null means use flow template
 * @param replyToId      optional message ID to reply to in a thread
 */
public record TeamsSendCommand(
        String flowKey,
        String channelId,
        Map<String, Object> model,
        String textOverride,
        String replyToId) implements SendCommand {

    public TeamsSendCommand {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        Objects.requireNonNull(channelId, "channelId must not be null");
        if (channelId.isBlank()) throw new IllegalArgumentException("channelId must not be blank");
        model = model != null ? Map.copyOf(model) : Map.of();
    }

    public static TeamsSendCommandBuilder builder(String flowKey) {
        return new TeamsSendCommandBuilder(flowKey);
    }
}
