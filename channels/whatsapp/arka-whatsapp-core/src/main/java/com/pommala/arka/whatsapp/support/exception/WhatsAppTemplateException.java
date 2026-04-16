package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.whatsapp.support.exception.WhatsAppApplicationException;

/**
 * Thrown when a Meta template fails approval check, language mismatch, or
 * variable binding. Maps to HTTP 422. Not retriable — template or variables
 * must be corrected.
 */
public class WhatsAppTemplateException extends WhatsAppApplicationException {
    public WhatsAppTemplateException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppTemplateException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppTemplateException(ApplicationCode code) { super(code); }
}
