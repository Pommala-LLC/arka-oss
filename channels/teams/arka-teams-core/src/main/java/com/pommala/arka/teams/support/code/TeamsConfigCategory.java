package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ConfigCategory;

/**
 * Configuration categories for Teams-specific configuration exceptions.
 * Used by the Teams exception handler for exhaustive HTTP code mapping.
 */
public enum TeamsConfigCategory implements ConfigCategory {

    /** Flow definition: flow key, delivery mode, template ref. */
    FLOW("Flow definition"),

    /** Sender identity: service principal, tenant ID, from name. */
    SENDER("Sender identity"),

    /** Routing target: team ID, channel ID, or chat ID. */
    CHANNEL("Channel routing"),

    /** Adaptive Card: template ref, card schema, variable binding. */
    TEMPLATE("Adaptive Card template"),

    /** Graph API transport: credentials, throttling, auth. */
    TRANSPORT("Graph API transport");

    private final String displayName;
    TeamsConfigCategory(String displayName) { this.displayName = displayName; }
    @Override public String displayName() { return displayName; }
}
