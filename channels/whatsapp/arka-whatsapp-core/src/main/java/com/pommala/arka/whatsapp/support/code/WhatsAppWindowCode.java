package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * 24-hour conversation window codes. Prefix: {@code WA-WINDOW-*}
 * Separate family — not merged into core or transport codes.
 */
public enum WhatsAppWindowCode implements ApplicationCode {

    WINDOW_ACTIVE           ("WA-WINDOW-1000","24-hour session window is active"),
    WINDOW_EXPIRED          ("WA-WINDOW-1001","24-hour session window has expired"),
    WINDOW_CHECK_FAILED     ("WA-WINDOW-5000","Conversation window check failed"),
    WINDOW_DATA_UNAVAILABLE ("WA-WINDOW-5001","No inbound message data available");

    private final String code; private final String defaultMessage;
    WhatsAppWindowCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
