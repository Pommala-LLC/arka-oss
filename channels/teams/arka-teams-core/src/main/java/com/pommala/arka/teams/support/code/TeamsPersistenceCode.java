package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Teams DB persistence codes. Prefix: {@code TEAMS-PERSIST-*} */
public enum TeamsPersistenceCode implements ApplicationCode {

    DB_READ_FAILED       ("TEAMS-PERSIST-5000", "DB read failed"),
    DB_READ_TIMEOUT      ("TEAMS-PERSIST-5001", "DB read timed out"),
    MAPPER_FAILED        ("TEAMS-PERSIST-5002", "Mapper conversion failed"),
    DB_UNAVAILABLE       ("TEAMS-PERSIST-5003", "DB unavailable"),
    DB_DATA_INCONSISTENT ("TEAMS-PERSIST-5004", "DB data inconsistent");

    private final String code; private final String defaultMessage;
    TeamsPersistenceCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
