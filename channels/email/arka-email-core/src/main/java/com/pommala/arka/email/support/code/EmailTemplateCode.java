package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Template rendering codes. Used by {@code ThymeleafTemplateRenderer} and
 * {@code ThymeleafTemplateProbe}. All codes carry {@link EmailConfigCategory#TEMPLATE}.
 * Prefix: {@code EMAIL-TPL-*}
 */
public enum EmailTemplateCode implements ApplicationCode {

    TEMPLATE_RENDER_FAILED ("EMAIL-TPL-5000", "Template rendering failed"),
    TEMPLATE_NOT_RESOLVABLE("EMAIL-TPL-5001", "Template not resolvable"),
    TEMPLATE_VARIABLE_ERROR("EMAIL-TPL-5002", "Template variable error");

    private final String code;
    private final String defaultMessage;

    EmailTemplateCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
