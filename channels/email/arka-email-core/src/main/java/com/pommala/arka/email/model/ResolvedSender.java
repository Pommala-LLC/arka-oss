package com.pommala.arka.email.model;

import java.util.Objects;

/**
 * Immutable resolved sender identity for an email flow.
 *
 * @param from        the sender email address, never null or blank
 * @param displayName sender display name for RFC-5322 formatting; null means no display name
 * @param replyTo     reply-to address; null means no reply-to header added
 */
public record ResolvedSender(String from, String displayName, String replyTo) {

    public ResolvedSender {
        Objects.requireNonNull(from, "from must not be null");
        if (from.isBlank()) throw new IllegalArgumentException("from must not be blank");
        // Null normalisation: null → empty string for display name and replyTo
        displayName = displayName != null ? displayName : "";
        replyTo     = replyTo     != null ? replyTo     : "";
    }

    /** Returns true if a non-blank reply-to address is configured. */
    public boolean hasReplyTo() { return !replyTo.isBlank(); }

    /** Returns true if a non-blank display name is configured. */
    public boolean hasDisplayName() { return !displayName.isBlank(); }

    /**
     * Returns the RFC-5322 formatted sender string.
     * Returns {@code "Display Name <from>"} when displayName is present,
     * or just the {@code from} address otherwise.
     */
    public String formatted() {
        return hasDisplayName() ? displayName + " <" + from + ">" : from;
    }
}
