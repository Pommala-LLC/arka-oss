package com.pommala.arka.slack.api;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.slack.api.builder.SlackSendCommandBuilder;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable Slack send command.
 *
 * @param flowKey        the flow key identifying the delivery configuration
 * @param channelId      target Slack channel or user ID
 * @param model          template variables, never null, may be empty
 * @param textOverride   optional plain-text override; null means use flow template
 * @param threadTs       optional thread timestamp for reply threading
 */
public record SlackSendCommand(
        String flowKey,
        String channelId,
        Map<String, Object> model,
        String textOverride,
        String threadTs) implements SendCommand {

    public SlackSendCommand {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        Objects.requireNonNull(channelId, "channelId must not be null");
        if (channelId.isBlank()) throw new IllegalArgumentException("channelId must not be blank");
        model = model != null ? Map.copyOf(model) : Map.of();
    }

    public static SlackSendCommandBuilder builder(String flowKey) {
        return new SlackSendCommandBuilder(flowKey);
    }
}
