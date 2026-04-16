package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp message construction codes. Prefix: {@code WA-MSG-*} */
public enum WhatsAppMessageCode implements ApplicationCode {

    MESSAGE_BUILD_FAILED    ("WA-MSG-5000","Message construction failed"),
    MEDIA_BINDING_FAILED    ("WA-MSG-5001","Media attachment binding failed"),
    SENDER_RESOLUTION_FAILED("WA-MSG-5002","Sender resolution failed"),
    INTERACTIVE_BUILD_FAILED("WA-MSG-5003","Interactive message component build failed");

    private final String code; private final String defaultMessage;
    WhatsAppMessageCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
