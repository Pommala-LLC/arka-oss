package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTaskInterruptedException;

/**
 * Thrown when a WhatsApp virtual thread task is interrupted.
 * Maps to HTTP 503.
 * Always call {@code Thread.currentThread().interrupt()} before throwing.
 */
public class WhatsAppTaskInterruptedException extends ArkaTaskInterruptedException {
    public WhatsAppTaskInterruptedException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppTaskInterruptedException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppTaskInterruptedException(ApplicationCode code) { super(code); }
}
