package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp DB persistence codes. Prefix: {@code WA-PERSIST-*} */
public enum WhatsAppPersistenceCode implements ApplicationCode {

    DB_READ_FAILED      ("WA-PERSIST-5000","DB read failed"),
    DB_READ_TIMEOUT     ("WA-PERSIST-5001","DB read timed out"),
    MAPPER_FAILED       ("WA-PERSIST-5002","Mapper conversion failed"),
    DB_UNAVAILABLE      ("WA-PERSIST-5003","DB unavailable"),
    DB_DATA_INCONSISTENT("WA-PERSIST-5004","DB data inconsistent");

    private final String code; private final String defaultMessage;
    WhatsAppPersistenceCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
