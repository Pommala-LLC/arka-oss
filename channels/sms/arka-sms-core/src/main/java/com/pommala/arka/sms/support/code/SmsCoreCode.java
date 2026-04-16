package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS service lifecycle codes. Prefix: {@code SMS-CORE-*} */
public enum SmsCoreCode implements ApplicationCode {
    SEND_STARTED ("SMS-CORE-1000","Send workflow started"),
    SEND_COMPLETED("SMS-CORE-1001","Send workflow completed"),
    SEND_FAILED  ("SMS-CORE-5000","Send workflow failed"),
    UNEXPECTED   ("SMS-CORE-5001","Unexpected error");
    private final String code; private final String defaultMessage;
    SmsCoreCode(String c, String m) { code=c; defaultMessage=m; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
