package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Teams batch dispatch codes. Prefix: {@code TEAMS-BATCH-*} */
public enum TeamsBatchCode implements ApplicationCode {

    BATCH_STARTED   ("TEAMS-BATCH-1000", "Batch dispatch started"),
    BATCH_COMPLETED ("TEAMS-BATCH-1001", "Batch dispatch completed"),
    BATCH_PARTIAL   ("TEAMS-BATCH-1002", "Batch dispatch completed with partial success"),
    BATCH_EMPTY     ("TEAMS-BATCH-4000", "Batch target list is empty"),
    BATCH_NULL      ("TEAMS-BATCH-4001", "Batch target list is null"),
    RECIPIENT_FAILED("TEAMS-BATCH-5001", "Per-recipient send failed");

    private final String code; private final String defaultMessage;
    TeamsBatchCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
