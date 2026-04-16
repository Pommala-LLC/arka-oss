package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/**
 * Thrown when a Meta Cloud API transport operation fails.
 * HTTP mapping is code-based: 429 (rate limit), 503 (unavailable), 504 (timeout via subtype), 502 (all others).
 */
public class WhatsAppTransportException extends ArkaTransportException {
    public WhatsAppTransportException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppTransportException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppTransportException(ApplicationCode code) { super(code); }
}
