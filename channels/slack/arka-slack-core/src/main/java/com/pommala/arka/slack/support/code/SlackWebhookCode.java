package com.pommala.arka.slack.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Slack event / webhook codes. Prefix: {@code SLACK-WEBHOOK-*} */
public enum SlackWebhookCode implements ApplicationCode {
    EVENT_RECEIVED      ("SLACK-WEBHOOK-1000","Slack event received"),
    DELIVERY_CONFIRMED  ("SLACK-WEBHOOK-1001","Message delivery confirmed"),
    SIGNATURE_INVALID   ("SLACK-WEBHOOK-4000","Slack request signature verification failed"),
    PAYLOAD_MALFORMED   ("SLACK-WEBHOOK-4001","Slack event payload could not be parsed"),
    CHALLENGE_FAILED    ("SLACK-WEBHOOK-4002","URL verification challenge failed");
    private final String code; private final String defaultMessage;
    SlackWebhookCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
