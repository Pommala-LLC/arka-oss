package com.pommala.arka.sms.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** SMS transport codes. Prefix: {@code SMS-TRN-*} */
public enum SmsTransportCode implements ApplicationCode {
    SEND_FAILED        ("SMS-TRN-5020","SMS send failed",           false),
    API_UNAVAILABLE    ("SMS-TRN-5031","SMS API unavailable",       false),
    API_TIMEOUT        ("SMS-TRN-5040","SMS API request timed out", true),
    AUTH_FAILED        ("SMS-TRN-5043","SMS API authentication failed", false),
    CONNECTION_REFUSED ("SMS-TRN-5045","SMS API connection refused", true),
    RATE_LIMITED       ("SMS-TRN-5050","SMS API rate limit exceeded",true),
    CARRIER_REJECTED   ("SMS-TRN-5051","Carrier rejected the message",false),
    INVALID_NUMBER     ("SMS-TRN-5052","Invalid phone number",       false);
    private final String code; private final String defaultMessage; private final boolean retriable;
    SmsTransportCode(String c, String m, boolean r) { code=c; defaultMessage=m; retriable=r; }
    @Override public String code() { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
    @Override public boolean retriable() { return retriable; }
}
