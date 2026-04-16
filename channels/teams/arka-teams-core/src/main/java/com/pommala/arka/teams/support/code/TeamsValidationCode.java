package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Teams startup validation codes. Prefix: {@code TEAMS-VAL-*}
 * Every code carries a {@link TeamsConfigCategory}.
 */
public enum TeamsValidationCode implements ApplicationCode {

    VALIDATION_FAILED          ("TEAMS-VAL-4000", "Startup validation failed",                    TeamsConfigCategory.FLOW),
    FLOW_KEY_MISSING           ("TEAMS-VAL-4001", "Flow key is missing",                          TeamsConfigCategory.FLOW),
    DELIVERY_MODE_MISSING      ("TEAMS-VAL-4002", "Flow delivery mode is missing",                TeamsConfigCategory.FLOW),
    TEMPLATE_REF_MISSING       ("TEAMS-VAL-4003", "Flow template reference is missing",           TeamsConfigCategory.TEMPLATE),
    SENDER_REF_MISSING         ("TEAMS-VAL-4004", "Flow sender reference is missing",             TeamsConfigCategory.SENDER),
    SENDER_REF_UNRESOLVABLE    ("TEAMS-VAL-4005", "Sender reference could not be resolved",       TeamsConfigCategory.SENDER),
    TENANT_ID_MISSING          ("TEAMS-VAL-4006", "Entra ID tenant ID is missing",               TeamsConfigCategory.SENDER),
    CLIENT_ID_MISSING          ("TEAMS-VAL-4007", "Entra ID client ID is missing",               TeamsConfigCategory.SENDER),
    CLIENT_SECRET_MISSING      ("TEAMS-VAL-4008", "Entra ID client secret is missing",           TeamsConfigCategory.SENDER),
    CHANNEL_TARGET_MISSING     ("TEAMS-VAL-4009", "Team ID + channel ID or chat ID is missing",  TeamsConfigCategory.CHANNEL),
    TEMPLATE_REF_UNRESOLVABLE  ("TEAMS-VAL-4010", "Template reference could not be resolved",    TeamsConfigCategory.TEMPLATE),
    CARD_SCHEMA_INVALID        ("TEAMS-VAL-4011", "Adaptive Card schema is invalid",             TeamsConfigCategory.TEMPLATE);

    private final String code; private final String defaultMessage;
    private final TeamsConfigCategory configCategory;

    TeamsValidationCode(String c, String m, TeamsConfigCategory cat) {
        code = c; defaultMessage = m; configCategory = cat;
    }

    @Override public String code()              { return code; }
    @Override public String defaultMessage()    { return defaultMessage; }
    public TeamsConfigCategory configCategory() { return configCategory; }
}
