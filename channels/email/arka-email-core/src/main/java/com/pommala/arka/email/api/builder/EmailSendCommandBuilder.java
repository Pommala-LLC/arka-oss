package com.pommala.arka.email.api.builder;

import com.pommala.arka.email.api.EmailSendCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Fluent builder for {@link EmailSendCommand}.
 *
 * <p>Provides ergonomic accumulation of recipient addresses and template
 * variables. Multiple calls to {@link #to(String...)}, {@link #cc(String...)},
 * {@link #bcc(String...)}, and {@link #model(String, Object)} accumulate —
 * they do not replace.
 *
 * @see EmailSendCommand
 */
public final class EmailSendCommandBuilder {

    private final String flowKey;
    private final List<String> to  = new ArrayList<>();
    private final List<String> cc  = new ArrayList<>();
    private final List<String> bcc = new ArrayList<>();
    private final Map<String, Object> model = new HashMap<>();
    private String subjectOverride;

    /**
     * Creates a builder with the required flow key.
     *
     * @param flowKey the flow key, never null or blank
     * @throws NullPointerException     if flowKey is null
     * @throws IllegalArgumentException if flowKey is blank
     */
    public EmailSendCommandBuilder(String flowKey) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        this.flowKey = flowKey;
    }

    /** Adds one or more primary recipient addresses. Accumulates across calls. */
    public EmailSendCommandBuilder to(String... addresses) {
        to.addAll(List.of(addresses));
        return this;
    }

    /** Adds one or more CC addresses. Accumulates across calls. */
    public EmailSendCommandBuilder cc(String... addresses) {
        cc.addAll(List.of(addresses));
        return this;
    }

    /** Adds one or more BCC addresses. Accumulates across calls. */
    public EmailSendCommandBuilder bcc(String... addresses) {
        bcc.addAll(List.of(addresses));
        return this;
    }

    /** Adds a single template variable. Accumulates across calls. */
    public EmailSendCommandBuilder model(String key, Object value) {
        model.put(key, value);
        return this;
    }

    /** Adds all entries from the given map. Accumulates across calls. */
    public EmailSendCommandBuilder model(Map<String, Object> variables) {
        model.putAll(variables);
        return this;
    }

    /** Overrides the flow-level subject. Optional — null means use flow subject. */
    public EmailSendCommandBuilder subjectOverride(String subject) {
        this.subjectOverride = subject;
        return this;
    }

    /**
     * Constructs the immutable {@link EmailSendCommand}.
     *
     * @return the built command, never null
     */
    public EmailSendCommand build() {
        return new EmailSendCommand(flowKey, List.copyOf(to), List.copyOf(cc),
                List.copyOf(bcc), Map.copyOf(model), subjectOverride);
    }
}
