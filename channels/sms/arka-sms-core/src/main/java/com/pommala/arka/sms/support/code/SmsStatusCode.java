package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS delivery status callback codes. Prefix: {@code SMS-STATUS-*} */
public enum SmsStatusCode implements ApplicationCode {
    STATUS_DELIVERED    ("SMS-STATUS-1000","SMS delivery confirmed"),
    STATUS_SENT         ("SMS-STATUS-1001","SMS sent to carrier"),
    STATUS_FAILED       ("SMS-STATUS-1002","SMS delivery failed"),
    STATUS_UNDELIVERED  ("SMS-STATUS-1003","SMS undelivered by carrier"),
    SIGNATURE_INVALID   ("SMS-STATUS-4000","Delivery status signature invalid"),
    PAYLOAD_MALFORMED   ("SMS-STATUS-4001","Delivery status payload could not be parsed");
    private final String code; private final String defaultMessage;
    SmsStatusCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
