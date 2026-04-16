package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Microsoft Graph API transport codes. Prefix: {@code TEAMS-TRN-*}
 *
 * <p>HTTP mapping:</p>
 * <ul>
 *   <li>{@link #GRAPH_TIMEOUT}    → 504</li>
 *   <li>{@link #GRAPH_UNAVAILABLE}→ 503</li>
 *   <li>{@link #GRAPH_THROTTLED}  → 429</li>
 *   <li>All others                → 502</li>
 * </ul>
 *
 * <p>Auth failures ({@link #AUTH_TOKEN_FAILED}, {@link #AUTH_PERMISSION_DENIED})
 * are non-retriable — Entra ID configuration must be corrected.</p>
 */
public enum TeamsTransportCode implements ApplicationCode {

    GRAPH_SEND_FAILED      ("TEAMS-TRN-5020", "Graph API send failed",                    false),
    GRAPH_UNAVAILABLE      ("TEAMS-TRN-5031", "Graph API unavailable",                    false),
    GRAPH_TIMEOUT          ("TEAMS-TRN-5040", "Graph API request timed out",              true),
    AUTH_TOKEN_FAILED      ("TEAMS-TRN-5043", "Entra ID token acquisition failed",        false),
    GRAPH_TLS_FAILED       ("TEAMS-TRN-5044", "Graph API TLS handshake failed",           false),
    GRAPH_CONNECTION_REFUSED("TEAMS-TRN-5045","Graph API connection refused",             true),
    AUTH_PERMISSION_DENIED ("TEAMS-TRN-5046", "Insufficient Graph API permissions",       false),
    GRAPH_THROTTLED        ("TEAMS-TRN-5050", "Graph API throttled — Retry-After applies",true),
    BOT_BLOCKED            ("TEAMS-TRN-5051", "Bot blocked by recipient or tenant policy",false),
    CHANNEL_MEMBERSHIP_REQUIRED("TEAMS-TRN-5052","Bot must be member of target channel",  false),
    GUEST_ACCESS_DENIED    ("TEAMS-TRN-5053", "Guest access denied by tenant policy",     false);

    private final String code; private final String defaultMessage; private final boolean retriable;

    TeamsTransportCode(String c, String m, boolean r) {
        code = c; defaultMessage = m; retriable = r;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
    @Override public boolean retriable()     { return retriable; }
}
