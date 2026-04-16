package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp service lifecycle codes. Prefix: {@code WA-CORE-*} */
public enum WhatsAppCoreCode implements ApplicationCode {

    SEND_STARTED ("WA-CORE-1000", "Send workflow started"),
    SEND_COMPLETED("WA-CORE-1001","Send workflow completed"),
    SEND_FAILED  ("WA-CORE-1002", "Send workflow failed");

    private final String code; private final String defaultMessage;
    WhatsAppCoreCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
