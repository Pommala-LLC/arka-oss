package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaAsyncTimeoutException;

/** Thrown when WhatsApp async dispatch exceeds its deadline. Maps to HTTP 504. */
public class WhatsAppAsyncTimeoutException extends ArkaAsyncTimeoutException {
    public WhatsAppAsyncTimeoutException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppAsyncTimeoutException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppAsyncTimeoutException(ApplicationCode code) { super(code); }
}
