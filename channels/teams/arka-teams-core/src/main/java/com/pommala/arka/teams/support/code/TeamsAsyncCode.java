package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Teams async execution codes. Prefix: {@code TEAMS-ASYNC-*} */
public enum TeamsAsyncCode implements ApplicationCode {

    DISPATCH_REJECTED  ("TEAMS-ASYNC-5000", "Dispatch rejected by executor"),
    TASK_INTERRUPTED   ("TEAMS-ASYNC-5001", "Task interrupted"),
    TASK_CANCELLED     ("TEAMS-ASYNC-5002", "Task cancelled"),
    SINGLE_SEND_TIMEOUT("TEAMS-ASYNC-5040", "Single send timed out"),
    BATCH_TIMEOUT      ("TEAMS-ASYNC-5041", "Batch dispatch timed out"),
    RECIPIENT_TIMEOUT  ("TEAMS-ASYNC-5042", "Recipient task timed out");

    private final String code; private final String defaultMessage;
    TeamsAsyncCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
