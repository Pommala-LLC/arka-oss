package com.pommala.arka.email.api;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.email.api.builder.EmailSendCommandBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable email send command.
 *
 * <p>All collection fields use {@code List.copyOf()} and {@code Map.copyOf()}.
 * This record is fully immutable after construction.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null, non-blank.</li>
 *   <li>{@code to} — required, non-null, defensively copied. May be empty (validated at send time).</li>
 *   <li>{@code cc} — nullable, normalised to empty list.</li>
 *   <li>{@code bcc} — nullable, normalised to empty list.</li>
 *   <li>{@code model} — nullable, normalised to empty map. Template variables.</li>
 *   <li>{@code subjectOverride} — nullable. Overrides the flow-level subject when set.</li>
 * </ul>
 *
 * <h3>Business rules enforced at construction:</h3>
 * <ul>
 *   <li>{@code flowKey} must not be null or blank.</li>
 *   <li>Null collections normalised to empty — never remain null.</li>
 * </ul>
 *
 * @param flowKey         the flow key identifying the delivery configuration, never null or blank
 * @param to              primary recipient addresses, never null, may be empty
 * @param cc              CC addresses, never null, may be empty
 * @param bcc             BCC addresses, never null, may be empty
 * @param model           template variables, never null, may be empty
 * @param subjectOverride optional subject override; null means use flow subject
 * @see EmailSendCommandBuilder
 */
public record EmailSendCommand(
        String flowKey,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        Map<String, Object> model,
        String subjectOverride) implements SendCommand {

    public EmailSendCommand {
        // Business rule: flowKey is always required.
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) {
            throw new IllegalArgumentException("flowKey must not be blank");
        }
        // Null collection normalisation: null → empty, never remain null.
        to    = to    != null ? List.copyOf(to)          : List.of();
        cc    = cc    != null ? List.copyOf(cc)          : List.of();
        bcc   = bcc   != null ? List.copyOf(bcc)         : List.of();
        model = model != null ? Map.copyOf(model)        : Map.of();
    }

    /** Returns a builder pre-set with the given flow key. */
    public static EmailSendCommandBuilder builder(String flowKey) {
        return new EmailSendCommandBuilder(flowKey);
    }
}
