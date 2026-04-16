package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ConfigCategory;

/** Configuration categories for Slack-specific configuration exceptions. */
public enum SlackConfigCategory implements ConfigCategory {
    FLOW("Flow definition"), SENDER("Sender / bot identity"),
    CHANNEL("Channel routing"), TRANSPORT("Transport");
    private final String displayName;
    SlackConfigCategory(String n) { displayName = n; }
    @Override public String displayName() { return displayName; }
}
