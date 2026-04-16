package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Teams service lifecycle codes. Prefix: {@code TEAMS-CORE-*} */
public enum TeamsCoreCode implements ApplicationCode {

    SEND_STARTED ("TEAMS-CORE-1000", "Send workflow started"),
    SEND_COMPLETED("TEAMS-CORE-1001","Send workflow completed"),
    SEND_FAILED  ("TEAMS-CORE-5000", "Send workflow failed"),
    UNEXPECTED   ("TEAMS-CORE-5001", "Unexpected error");

    private final String code; private final String defaultMessage;
    TeamsCoreCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
