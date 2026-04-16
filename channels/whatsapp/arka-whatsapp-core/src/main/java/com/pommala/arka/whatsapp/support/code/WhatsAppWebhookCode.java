package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp webhook / delivery status codes. Prefix: {@code WA-WEBHOOK-*} */
public enum WhatsAppWebhookCode implements ApplicationCode {

    WEBHOOK_RECEIVED  ("WA-WEBHOOK-1000","Webhook event received"),
    STATUS_DELIVERED  ("WA-WEBHOOK-1001","Message delivery confirmed"),
    STATUS_READ       ("WA-WEBHOOK-1002","Message read confirmed"),
    STATUS_FAILED     ("WA-WEBHOOK-1003","Message delivery failed"),
    SIGNATURE_INVALID ("WA-WEBHOOK-4000","Webhook signature verification failed"),
    PAYLOAD_MALFORMED ("WA-WEBHOOK-4001","Webhook payload could not be parsed");

    private final String code; private final String defaultMessage;
    WhatsAppWebhookCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
