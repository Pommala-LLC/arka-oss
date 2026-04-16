package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ConfigCategory;

/** Configuration categories for SMS-specific configuration exceptions. */
public enum SmsConfigCategory implements ConfigCategory {
    FLOW("Flow definition"), SENDER("Sender identity"),
    TRANSPORT("Transport"), DESTINATION("Destination");
    private final String displayName;
    SmsConfigCategory(String n) { displayName = n; }
    @Override public String displayName() { return displayName; }
}
