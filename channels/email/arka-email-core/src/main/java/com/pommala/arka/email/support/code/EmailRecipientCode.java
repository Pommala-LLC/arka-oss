package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Recipient normalization codes. Used by {@code DefaultRecipientNormalizer}.
 * Prefix: {@code EMAIL-RCP-*}
 */
public enum EmailRecipientCode implements ApplicationCode {

    INVALID_ADDRESS_FORMAT    ("EMAIL-RCP-4000", "Recipient address format is invalid"),
    NO_VALID_PRIMARY_RECIPIENT("EMAIL-RCP-4001", "No valid primary recipient after policy resolution"),
    DUPLICATE_COLLAPSED       ("EMAIL-RCP-4002", "Duplicate recipient collapsed");

    private final String code;
    private final String defaultMessage;

    EmailRecipientCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
