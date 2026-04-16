package com.pommala.arka.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Shared typed-client descriptor codes. Prefix: {@code ARKA-CLIENT-*}
 */
public enum ArkaClientCode implements ApplicationCode {

    DESCRIPTOR_DUPLICATE   ("ARKA-CLIENT-4000", "Duplicate ChannelClientDescriptor for same channel key"),
    CLIENT_TIMEOUT         ("ARKA-CLIENT-5040", "Arka API request timed out"),
    CLIENT_CONNECTION_FAILED("ARKA-CLIENT-5045","Arka API connection failed"),
    CLIENT_REQUEST_FAILED  ("ARKA-CLIENT-5020", "Arka API request failed");

    private final String code;
    private final String defaultMessage;

    ArkaClientCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
