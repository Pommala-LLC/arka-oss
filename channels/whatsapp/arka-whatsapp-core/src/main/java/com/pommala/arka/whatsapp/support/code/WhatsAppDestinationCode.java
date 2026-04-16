package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp destination (phone number) codes. Prefix: {@code WA-DST-*} */
public enum WhatsAppDestinationCode implements ApplicationCode {

    DESTINATION_INVALID    ("WA-DST-4000","Destination phone number is invalid"),
    DESTINATION_NOT_E164   ("WA-DST-4001","Destination is not in E.164 format"),
    DESTINATION_OPT_IN_MISSING("WA-DST-4002","Destination has not opted in"),
    DESTINATION_BLOCKED    ("WA-DST-4003","Destination is on the blocked number list"),
    NO_VALID_DESTINATION   ("WA-DST-4004","No valid destination after policy resolution"),
    ALL_DESTINATIONS_FILTERED("WA-DST-4005","All destinations were filtered by policy");

    private final String code; private final String defaultMessage;
    WhatsAppDestinationCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
