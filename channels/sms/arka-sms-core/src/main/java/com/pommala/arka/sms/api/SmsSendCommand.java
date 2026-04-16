package com.pommala.arka.sms.api;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.sms.api.builder.SmsSendCommandBuilder;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable SMS send command.
 *
 * @param flowKey       the flow key identifying the delivery configuration
 * @param to            destination phone number in E.164 format
 * @param model         template variables, never null, may be empty
 * @param bodyOverride  optional body text override; null means use flow template
 * @param fromOverride  optional sender number override; null means use flow sender
 */
public record SmsSendCommand(
        String flowKey,
        String to,
        Map<String, Object> model,
        String bodyOverride,
        String fromOverride) implements SendCommand {

    public SmsSendCommand {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        Objects.requireNonNull(to, "to must not be null");
        if (to.isBlank()) throw new IllegalArgumentException("to must not be blank");
        model = model != null ? Map.copyOf(model) : Map.of();
    }

    public static SmsSendCommandBuilder builder(String flowKey) {
        return new SmsSendCommandBuilder(flowKey);
    }
}
