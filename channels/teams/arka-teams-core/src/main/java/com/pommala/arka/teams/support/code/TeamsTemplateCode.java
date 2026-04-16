package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Adaptive Card template codes. Prefix: {@code TEAMS-TPL-*}
 * All codes carry {@link TeamsConfigCategory#TEMPLATE}.
 */
public enum TeamsTemplateCode implements ApplicationCode {

    CARD_RENDER_FAILED    ("TEAMS-TPL-5000", "Adaptive Card rendering failed"),
    CARD_NOT_RESOLVABLE   ("TEAMS-TPL-5001", "Adaptive Card template not resolvable"),
    CARD_VARIABLE_ERROR   ("TEAMS-TPL-5002", "Adaptive Card variable binding error"),
    CARD_SCHEMA_VIOLATION ("TEAMS-TPL-5003", "Adaptive Card schema validation failed"),
    CARD_VERSION_UNSUPPORTED("TEAMS-TPL-5004","Adaptive Card schema version unsupported");

    private final String code; private final String defaultMessage;
    TeamsTemplateCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
