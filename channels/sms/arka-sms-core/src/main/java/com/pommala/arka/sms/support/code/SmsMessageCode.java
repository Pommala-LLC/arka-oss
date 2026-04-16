package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS message construction codes. Prefix: {@code SMS-MSG-*} */
public enum SmsMessageCode implements ApplicationCode {
    MESSAGE_BUILD_FAILED ("SMS-MSG-5000","Message build failed"),
    SEGMENT_COUNT_FAILED ("SMS-MSG-5001","Segment count calculation failed"),
    ENCODING_FAILED      ("SMS-MSG-5002","Message encoding failed");
    private final String code; private final String defaultMessage;
    SmsMessageCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
