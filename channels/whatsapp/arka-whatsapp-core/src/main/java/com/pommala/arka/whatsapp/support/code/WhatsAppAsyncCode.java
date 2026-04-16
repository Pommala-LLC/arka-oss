package com.pommala.arka.whatsapp.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/** WhatsApp async execution codes. Prefix: {@code WA-ASYNC-*} */
public enum WhatsAppAsyncCode implements ApplicationCode {

    DISPATCH_REJECTED  ("WA-ASYNC-5000","Dispatch rejected by executor"),
    TASK_INTERRUPTED   ("WA-ASYNC-5001","Task interrupted"),
    TASK_CANCELLED     ("WA-ASYNC-5002","Task cancelled"),
    SINGLE_SEND_TIMEOUT("WA-ASYNC-5040","Single send timed out"),
    BATCH_TIMEOUT      ("WA-ASYNC-5041","Batch dispatch timed out"),
    RECIPIENT_TIMEOUT  ("WA-ASYNC-5042","Recipient task timed out");

    private final String code; private final String defaultMessage;
    WhatsAppAsyncCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
