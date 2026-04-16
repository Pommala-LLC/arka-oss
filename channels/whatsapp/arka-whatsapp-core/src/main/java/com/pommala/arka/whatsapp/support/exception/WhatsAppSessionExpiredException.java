package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.whatsapp.support.exception.WhatsAppApplicationException;

/**
 * Thrown when the 24-hour conversation window has expired for a session send.
 * Code: {@code WA-TRN-5053}. Maps to HTTP 422. Not retriable — caller must
 * switch to a template send or wait for a new inbound message.
 */
public class WhatsAppSessionExpiredException extends WhatsAppApplicationException {
    public WhatsAppSessionExpiredException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppSessionExpiredException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppSessionExpiredException(ApplicationCode code) { super(code); }
}
