package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Meta Cloud API transport codes. Prefix: {@code WA-TRN-*}
 *
 * <p>HTTP mapping:</p>
 * <ul>
 *   <li>{@link #API_TIMEOUT} → 504</li>
 *   <li>{@link #API_UNAVAILABLE} → 503</li>
 *   <li>{@link #API_RATE_LIMITED} → 429</li>
 *   <li>All others → 502</li>
 * </ul>
 */
public enum WhatsAppTransportCode implements ApplicationCode {

    API_SEND_FAILED        ("WA-TRN-5020","Meta API send failed",                    false),
    API_UNAVAILABLE        ("WA-TRN-5031","Meta API unavailable",                    false),
    API_TIMEOUT            ("WA-TRN-5040","Meta API request timed out",              true),
    API_AUTH_FAILED        ("WA-TRN-5043","Meta API authentication failed",          false),
    API_TLS_FAILED         ("WA-TRN-5044","Meta API TLS handshake failed",           false),
    API_CONNECTION_REFUSED ("WA-TRN-5045","Meta API connection refused",             true),
    API_SESSION_INIT_FAILED("WA-TRN-5046","Meta API client initialization failed",   false),
    API_RATE_LIMITED       ("WA-TRN-5050","Meta API rate limit exceeded",            true),
    API_INVALID_PHONE      ("WA-TRN-5051","Meta API rejected — invalid phone number",false),
    API_TEMPLATE_NOT_FOUND ("WA-TRN-5052","Meta API rejected — template not found",  false),
    API_SESSION_EXPIRED    ("WA-TRN-5053","Meta API rejected — session window expired",false);

    private final String code; private final String defaultMessage; private final boolean retriable;

    WhatsAppTransportCode(String c, String m, boolean r) {
        code = c; defaultMessage = m; retriable = r;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
    @Override public boolean retriable()     { return retriable; }
}
