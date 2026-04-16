package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack channel / DM routing codes. Prefix: {@code SLACK-CH-*} */
public enum SlackChannelCode implements ApplicationCode {
    CHANNEL_NOT_FOUND    ("SLACK-CH-4000","Slack channel not found"),
    CHANNEL_NOT_JOINED   ("SLACK-CH-4001","Bot not invited to channel"),
    DM_USER_NOT_FOUND    ("SLACK-CH-4002","DM target user not found"),
    NO_VALID_TARGET      ("SLACK-CH-4003","No valid channel or DM target");
    private final String code; private final String defaultMessage;
    SlackChannelCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
