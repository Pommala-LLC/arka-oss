package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Teams provider resolution codes. Prefix: {@code TEAMS-PROV-*} */
public enum TeamsProviderCode implements ApplicationCode {

    FLOW_NOT_FOUND_YAML ("TEAMS-PROV-4000", "Flow not found in YAML provider"),
    FLOW_NOT_FOUND_DB   ("TEAMS-PROV-4001", "Flow not found in DB provider"),
    FLOW_NOT_FOUND      ("TEAMS-PROV-4002", "Flow not found in any provider");

    private final String code; private final String defaultMessage;
    TeamsProviderCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
