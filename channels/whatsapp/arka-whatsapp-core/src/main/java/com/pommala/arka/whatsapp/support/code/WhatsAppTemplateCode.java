package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** Meta template management codes. Prefix: {@code WA-TPL-*} */
public enum WhatsAppTemplateCode implements ApplicationCode {

    TEMPLATE_NOT_APPROVED    ("WA-TPL-4000","Meta template is not in APPROVED status"),
    TEMPLATE_LANGUAGE_MISMATCH("WA-TPL-4001","Template language code does not match"),
    VARIABLE_BINDING_FAILED  ("WA-TPL-5000","Template variable binding failed"),
    TEMPLATE_RENDER_FAILED   ("WA-TPL-5001","Template rendering failed");

    private final String code; private final String defaultMessage;
    WhatsAppTemplateCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
