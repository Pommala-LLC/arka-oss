package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTaskCancelledException;

/** Thrown when a WhatsApp virtual thread task is cancelled. Maps to HTTP 503. */
public class WhatsAppTaskCancelledException extends ArkaTaskCancelledException {
    public WhatsAppTaskCancelledException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppTaskCancelledException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppTaskCancelledException(ApplicationCode code) { super(code); }
}
