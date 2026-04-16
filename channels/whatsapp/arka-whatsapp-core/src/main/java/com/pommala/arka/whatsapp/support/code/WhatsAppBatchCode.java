package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp batch dispatch codes. Prefix: {@code WA-BATCH-*} */
public enum WhatsAppBatchCode implements ApplicationCode {

    BATCH_STARTED   ("WA-BATCH-1000","Batch dispatch started"),
    BATCH_COMPLETED ("WA-BATCH-1001","Batch dispatch completed"),
    BATCH_PARTIAL   ("WA-BATCH-1002","Batch dispatch completed with partial success"),
    BATCH_EMPTY     ("WA-BATCH-4000","Batch destination list is empty"),
    BATCH_NULL      ("WA-BATCH-4001","Batch destination list is null"),
    RECIPIENT_FAILED("WA-BATCH-5001","Per-recipient send failed");

    private final String code; private final String defaultMessage;
    WhatsAppBatchCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
