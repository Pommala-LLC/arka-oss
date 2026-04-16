package com.pommala.arka.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Shared startup validation codes. Prefix: {@code ARKA-VAL-*}
 */
public enum ArkaValidationCode implements ApplicationCode {

    VALIDATION_FAILED("ARKA-VAL-4000", "Startup validation failed"),
    FLOW_KEY_MISSING ("ARKA-VAL-4001", "Flow key is missing");

    private final String code;
    private final String defaultMessage;

    ArkaValidationCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
