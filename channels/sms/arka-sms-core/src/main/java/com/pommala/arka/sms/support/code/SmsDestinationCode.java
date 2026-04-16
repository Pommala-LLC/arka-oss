package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS destination codes. Prefix: {@code SMS-DST-*} */
public enum SmsDestinationCode implements ApplicationCode {
    DESTINATION_INVALID    ("SMS-DST-4000","Destination phone number is invalid"),
    DESTINATION_NOT_E164   ("SMS-DST-4001","Destination is not in E.164 format"),
    DESTINATION_OPT_OUT    ("SMS-DST-4002","Destination has opted out"),
    NO_VALID_DESTINATION   ("SMS-DST-4003","No valid destination after policy resolution");
    private final String code; private final String defaultMessage;
    SmsDestinationCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
