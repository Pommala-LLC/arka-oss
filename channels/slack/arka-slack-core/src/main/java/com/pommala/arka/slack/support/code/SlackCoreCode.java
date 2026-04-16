package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack service lifecycle codes. Prefix: {@code SLACK-CORE-*} */
public enum SlackCoreCode implements ApplicationCode {
    SEND_STARTED ("SLACK-CORE-1000","Send workflow started"),
    SEND_COMPLETED("SLACK-CORE-1001","Send workflow completed"),
    SEND_FAILED  ("SLACK-CORE-5000","Send workflow failed"),
    UNEXPECTED   ("SLACK-CORE-5001","Unexpected error");
    private final String code; private final String defaultMessage;
    SlackCoreCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
