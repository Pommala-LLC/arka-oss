package com.pommala.arka.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Shared service lifecycle codes. Used by the generic send orchestration engine.
 * Prefix: {@code ARKA-CORE-*}
 */
public enum ArkaCoreCode implements ApplicationCode {

    SEND_STARTED  ("ARKA-CORE-1000", "Send workflow started"),
    SEND_SUCCEEDED("ARKA-CORE-1001", "Send workflow completed"),
    FLOW_RESOLVED ("ARKA-CORE-1002", "Flow resolution used"),
    SEND_FAILED   ("ARKA-CORE-5000", "Send workflow failed"),
    UNEXPECTED    ("ARKA-CORE-5001", "Unexpected error");

    private final String code;
    private final String defaultMessage;

    ArkaCoreCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
