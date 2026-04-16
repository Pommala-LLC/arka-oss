package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS startup validation codes. Prefix: {@code SMS-VAL-*} */
public enum SmsValidationCode implements ApplicationCode {
    VALIDATION_FAILED      ("SMS-VAL-4000","Startup validation failed"),
    FLOW_KEY_MISSING       ("SMS-VAL-4001","Flow key is missing"),
    SENDER_REF_MISSING     ("SMS-VAL-4002","Sender reference is missing"),
    SENDER_REF_UNRESOLVABLE("SMS-VAL-4003","Sender reference could not be resolved"),
    FROM_NUMBER_MISSING    ("SMS-VAL-4004","From number is missing");
    private final String code; private final String defaultMessage;
    SmsValidationCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
