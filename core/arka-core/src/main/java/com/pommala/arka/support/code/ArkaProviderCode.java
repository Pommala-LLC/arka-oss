package com.pommala.arka.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Shared provider resolution codes. Prefix: {@code ARKA-PROV-*}
 */
public enum ArkaProviderCode implements ApplicationCode {

    PROVIDER_AMBIGUOUS("ARKA-PROV-4000", "Ambiguous provider selection"),
    PROVIDER_DUPLICATE("ARKA-PROV-4001", "Duplicate provider candidates"),
    FLOW_NOT_FOUND    ("ARKA-PROV-4002", "Flow not found in any provider");

    private final String code;
    private final String defaultMessage;

    ArkaProviderCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
