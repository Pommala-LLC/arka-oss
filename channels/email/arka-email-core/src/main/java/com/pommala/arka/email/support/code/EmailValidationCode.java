package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Email startup validation codes. Used by {@code DefaultEmailFlowValidator}.
 * Every code carries a {@link EmailConfigCategory} for diagnostic grouping.
 * Prefix: {@code EMAIL-VAL-*}
 */
public enum EmailValidationCode implements ApplicationCode {

    VALIDATION_FAILED         ("EMAIL-VAL-4000", "Startup validation failed",                    EmailConfigCategory.FLOW),
    TEMPLATE_MISSING          ("EMAIL-VAL-4001", "Flow template is missing",                     EmailConfigCategory.FLOW),
    SUBJECT_MISSING           ("EMAIL-VAL-4002", "Flow subject is missing",                      EmailConfigCategory.FLOW),
    SENDER_REF_MISSING        ("EMAIL-VAL-4003", "Sender reference is missing",                  EmailConfigCategory.SENDER),
    SENDER_REF_UNRESOLVABLE   ("EMAIL-VAL-4004", "Sender reference could not be resolved",       EmailConfigCategory.SENDER),
    SENDER_FROM_INVALID       ("EMAIL-VAL-4005", "Sender from address is invalid",               EmailConfigCategory.SENDER),
    REPLY_TO_INVALID          ("EMAIL-VAL-4006", "Reply-to address is invalid",                  EmailConfigCategory.SENDER),
    ATTACHMENT_REF_UNRESOLVABLE("EMAIL-VAL-4007","Attachment reference could not be resolved",   EmailConfigCategory.ATTACHMENT),
    TEMPLATE_NOT_RESOLVABLE   ("EMAIL-VAL-4008", "Template could not be found",                  EmailConfigCategory.FLOW);

    private final String code;
    private final String defaultMessage;
    private final EmailConfigCategory configCategory;

    EmailValidationCode(String code, String defaultMessage, EmailConfigCategory configCategory) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.configCategory = configCategory;
    }

    @Override public String code()              { return code; }
    @Override public String defaultMessage()    { return defaultMessage; }
    public EmailConfigCategory configCategory() { return configCategory; }
}
