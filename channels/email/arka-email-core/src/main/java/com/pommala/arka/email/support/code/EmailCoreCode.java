package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Email service lifecycle codes. Used by {@code DefaultEmailSendHandler}.
 * Prefix: {@code EMAIL-CORE-*}
 */
public enum EmailCoreCode implements ApplicationCode {

    SEND_STARTED  ("EMAIL-CORE-1000", "Send workflow started"),
    SEND_SUCCEEDED("EMAIL-CORE-1001", "Send workflow completed"),
    FLOW_RESOLVED ("EMAIL-CORE-1002", "Flow resolution used"),
    SEND_FAILED   ("EMAIL-CORE-5000", "Send workflow failed"),
    UNEXPECTED    ("EMAIL-CORE-5001", "Unexpected error");

    private final String code;
    private final String defaultMessage;

    EmailCoreCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
