package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Teams message construction codes. Prefix: {@code TEAMS-MSG-*}
 * All codes carry {@link TeamsConfigCategory#TEMPLATE}.
 */
public enum TeamsMessageCode implements ApplicationCode {

    MESSAGE_BUILD_FAILED    ("TEAMS-MSG-5000", "Message construction failed"),
    ATTACHMENT_BUILD_FAILED ("TEAMS-MSG-5001", "Message attachment build failed"),
    SENDER_RESOLUTION_FAILED("TEAMS-MSG-5002", "Sender resolution failed"),
    MENTION_RESOLUTION_FAILED("TEAMS-MSG-5003","@mention target resolution failed");

    private final String code; private final String defaultMessage;
    TeamsMessageCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
