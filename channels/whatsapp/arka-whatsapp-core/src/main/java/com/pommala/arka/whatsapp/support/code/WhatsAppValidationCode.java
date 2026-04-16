package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * WhatsApp startup validation codes. Prefix: {@code WA-VAL-*}
 * Every code carries a {@link WhatsAppConfigCategory}.
 */
public enum WhatsAppValidationCode implements ApplicationCode {

    FRAMEWORK_VALIDATION           ("WA-VAL-0000","Framework validation error",             WhatsAppConfigCategory.FLOW),
    FLOW_KEY_MISSING               ("WA-VAL-4000","Flow key is missing",                    WhatsAppConfigCategory.FLOW),
    FLOW_TEMPLATE_REF_MISSING      ("WA-VAL-4001","Flow template reference is missing",     WhatsAppConfigCategory.TEMPLATE),
    FLOW_SENDER_REF_MISSING        ("WA-VAL-4002","Flow sender reference is missing",       WhatsAppConfigCategory.SENDER),
    SENDER_REF_UNRESOLVED          ("WA-VAL-4003","Sender reference could not be resolved", WhatsAppConfigCategory.SENDER),
    TEMPLATE_REF_UNRESOLVED        ("WA-VAL-4004","Template reference could not be resolved",WhatsAppConfigCategory.TEMPLATE),
    MEDIA_REF_UNRESOLVED           ("WA-VAL-4005","Media reference could not be resolved",  WhatsAppConfigCategory.MEDIA),
    PHONE_NUMBER_ID_MISSING        ("WA-VAL-4006","Sender phone-number-id is missing",      WhatsAppConfigCategory.SENDER),
    LANGUAGE_CODE_MISSING          ("WA-VAL-4007","Template language code is missing",      WhatsAppConfigCategory.TEMPLATE),
    DELIVERY_MODE_MISSING          ("WA-VAL-4008","Flow delivery-mode is missing",          WhatsAppConfigCategory.DELIVERY_MODE),
    DELIVERY_MODE_TEMPLATE_NO_REF  ("WA-VAL-4009","Delivery mode requires template but no template-ref", WhatsAppConfigCategory.DELIVERY_MODE),
    META_TEMPLATE_NAME_MISSING     ("WA-VAL-4010","Meta template name is missing",          WhatsAppConfigCategory.TEMPLATE);

    private final String code; private final String defaultMessage;
    private final WhatsAppConfigCategory configCategory;

    WhatsAppValidationCode(String c, String m, WhatsAppConfigCategory cat) {
        code = c; defaultMessage = m; configCategory = cat;
    }

    @Override public String code()              { return code; }
    @Override public String defaultMessage()    { return defaultMessage; }
    public WhatsAppConfigCategory configCategory() { return configCategory; }
}
