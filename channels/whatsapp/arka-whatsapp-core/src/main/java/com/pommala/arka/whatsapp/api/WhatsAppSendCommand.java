package com.pommala.arka.whatsapp.api;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.whatsapp.api.builder.WhatsAppSendCommandBuilder;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable WhatsApp send command.
 *
 * <p>All collection fields use {@code Map.copyOf()}.
 * This record is fully immutable after construction.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null, non-blank.</li>
 *   <li>{@code to} — required, non-null, non-blank. E.164 phone number.</li>
 *   <li>{@code model} — nullable, normalised to empty map. Template variables.</li>
 *   <li>{@code templateNameOverride} — nullable. Overrides the flow-level template name.</li>
 *   <li>{@code mediaUrl} — nullable. Media attachment URL for media messages.</li>
 *   <li>{@code bodyOverride} — nullable. Overrides template body for session messages.</li>
 * </ul>
 *
 * @param flowKey              the flow key identifying the delivery configuration
 * @param to                   destination phone number in E.164 format
 * @param model                template variables, never null, may be empty
 * @param templateNameOverride optional Meta template name override
 * @param mediaUrl             optional media attachment URL
 * @param bodyOverride         optional body text override for session messages
 * @see WhatsAppSendCommandBuilder
 */
public record WhatsAppSendCommand(
        String flowKey,
        String to,
        Map<String, Object> model,
        String templateNameOverride,
        String mediaUrl,
        String bodyOverride) implements SendCommand {

    public WhatsAppSendCommand {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        Objects.requireNonNull(to, "to must not be null");
        if (to.isBlank()) throw new IllegalArgumentException("to must not be blank");
        model = model != null ? Map.copyOf(model) : Map.of();
    }

    /** Returns a builder pre-set with the given flow key. */
    public static WhatsAppSendCommandBuilder builder(String flowKey) {
        return new WhatsAppSendCommandBuilder(flowKey);
    }
}
