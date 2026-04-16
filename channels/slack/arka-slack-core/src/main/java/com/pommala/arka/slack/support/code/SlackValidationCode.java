package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack startup validation codes. Prefix: {@code SLACK-VAL-*} */
public enum SlackValidationCode implements ApplicationCode {
    VALIDATION_FAILED        ("SLACK-VAL-4000","Startup validation failed"),
    FLOW_KEY_MISSING         ("SLACK-VAL-4001","Flow key is missing"),
    CHANNEL_TARGET_MISSING   ("SLACK-VAL-4002","Channel or DM target is missing"),
    BOT_TOKEN_MISSING        ("SLACK-VAL-4003","Bot token is missing"),
    WEBHOOK_URL_MISSING      ("SLACK-VAL-4004","Webhook URL is missing"),
    TRANSPORT_MODE_MISSING   ("SLACK-VAL-4005","Transport mode is missing (webhook or api)");
    private final String code; private final String defaultMessage;
    SlackValidationCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
