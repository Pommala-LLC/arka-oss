package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ConfigCategory;

/**
 * Configuration categories for WhatsApp-specific configuration exceptions.
 * Used by the WhatsApp exception handler for exhaustive HTTP code mapping.
 */
public enum WhatsAppConfigCategory implements ConfigCategory {

    FLOW         ("Flow definition"),
    SENDER       ("Sender identity"),
    TEMPLATE     ("Template"),
    MEDIA        ("Media"),
    DESTINATION  ("Destination"),
    DELIVERY_MODE("Delivery mode");

    private final String displayName;
    WhatsAppConfigCategory(String displayName) { this.displayName = displayName; }
    @Override public String displayName() { return displayName; }
}
