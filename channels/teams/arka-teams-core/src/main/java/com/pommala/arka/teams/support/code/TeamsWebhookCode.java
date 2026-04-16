package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Graph change notification codes for Teams delivery status.
 * Prefix: {@code TEAMS-WEBHOOK-*}
 */
public enum TeamsWebhookCode implements ApplicationCode {

    NOTIFICATION_RECEIVED  ("TEAMS-WEBHOOK-1000", "Graph change notification received"),
    STATUS_DELIVERED       ("TEAMS-WEBHOOK-1001", "Message delivery confirmed"),
    STATUS_FAILED          ("TEAMS-WEBHOOK-1002", "Message delivery failed"),
    SUBSCRIPTION_EXPIRED   ("TEAMS-WEBHOOK-1003", "Graph subscription expired — renewal required"),
    VALIDATION_TOKEN_FAILED("TEAMS-WEBHOOK-4000", "Lifecycle notification validation token invalid"),
    PAYLOAD_MALFORMED      ("TEAMS-WEBHOOK-4001", "Graph notification payload could not be parsed"),
    SUBSCRIPTION_RENEW_FAILED("TEAMS-WEBHOOK-5000","Graph subscription renewal failed");

    private final String code; private final String defaultMessage;
    TeamsWebhookCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
