package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ConfigCategory;

/**
 * Configuration categories for email-specific configuration exceptions.
 * Used by {@code EmailExceptionHandler} for exhaustive HTTP code mapping.
 *
 * <p>Every {@code EmailConfigurationException} carries one of these categories.
 * The exception handler does a complete switch — no default branch permitted.</p>
 */
public enum EmailConfigCategory implements ConfigCategory {

    /** Flow definition: template, subject, flow key. */
    FLOW("Flow definition"),

    /** Sender identity: from address, reply-to, sender-ref. */
    SENDER("Sender identity"),

    /** Attachment references. */
    ATTACHMENT("Attachment"),

    /** Template rendering. */
    TEMPLATE("Template"),

    /** Message construction. */
    MESSAGE("Message");

    private final String displayName;

    EmailConfigCategory(String displayName) { this.displayName = displayName; }

    @Override public String displayName() { return displayName; }
}
