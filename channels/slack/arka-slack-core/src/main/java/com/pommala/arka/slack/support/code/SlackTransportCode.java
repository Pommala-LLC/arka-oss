package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack transport codes. Prefix: {@code SLACK-TRN-*} */
public enum SlackTransportCode implements ApplicationCode {
    SEND_FAILED        ("SLACK-TRN-5020","Slack send failed",                 false),
    API_UNAVAILABLE    ("SLACK-TRN-5031","Slack API unavailable",             false),
    API_TIMEOUT        ("SLACK-TRN-5040","Slack API request timed out",       true),
    AUTH_FAILED        ("SLACK-TRN-5043","Slack authentication failed",       false),
    CONNECTION_REFUSED ("SLACK-TRN-5045","Slack API connection refused",      true),
    RATE_LIMITED       ("SLACK-TRN-5050","Slack API rate limit exceeded",     true),
    TOKEN_REVOKED      ("SLACK-TRN-5051","Slack bot token has been revoked",  false),
    NOT_IN_CHANNEL     ("SLACK-TRN-5052","Bot is not in the target channel",  false);
    private final String code; private final String defaultMessage; private final boolean retriable;
    SlackTransportCode(String c, String m, boolean r) { code=c; defaultMessage=m; retriable=r; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
    @Override public boolean retriable() { return retriable; }
}
