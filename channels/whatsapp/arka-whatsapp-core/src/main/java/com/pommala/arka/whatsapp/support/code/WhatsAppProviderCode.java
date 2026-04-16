package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp provider resolution codes. Prefix: {@code WA-PROV-*} */
public enum WhatsAppProviderCode implements ApplicationCode {

    FLOW_NOT_FOUND_YAML("WA-PROV-4000","Flow not found in YAML provider"),
    FLOW_NOT_FOUND_DB  ("WA-PROV-4001","Flow not found in DB provider"),
    FLOW_NOT_FOUND     ("WA-PROV-4002","Flow not found in any provider");

    private final String code; private final String defaultMessage;
    WhatsAppProviderCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
