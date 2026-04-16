package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack message construction codes. Prefix: {@code SLACK-MSG-*} */
public enum SlackMessageCode implements ApplicationCode {
    MESSAGE_BUILD_FAILED    ("SLACK-MSG-5000","Message construction failed"),
    BLOCK_KIT_INVALID       ("SLACK-MSG-5001","Block Kit structure is invalid"),
    ATTACHMENT_BUILD_FAILED ("SLACK-MSG-5002","Message attachment build failed");
    private final String code; private final String defaultMessage;
    SlackMessageCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
