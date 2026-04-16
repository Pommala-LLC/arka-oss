package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Message construction codes. Used by {@code DefaultEmailMessageBuilder}.
 * All codes carry {@link EmailConfigCategory#MESSAGE}.
 * Prefix: {@code EMAIL-MSG-*}
 */
public enum EmailMessageCode implements ApplicationCode {

    MESSAGE_BUILD_FAILED   ("EMAIL-MSG-5000", "Message build failed"),
    ATTACHMENT_LOAD_FAILED ("EMAIL-MSG-5001", "Attachment load failed"),
    MIME_CONSTRUCTION_FAILED("EMAIL-MSG-5002","MIME construction failed"),
    SENDER_FORMAT_INVALID  ("EMAIL-MSG-5003", "Sender format invalid");

    private final String code;
    private final String defaultMessage;

    EmailMessageCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
