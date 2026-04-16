package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.whatsapp.support.exception.WhatsAppTransportException;

/**
 * Thrown when a Meta Cloud API request times out. Code: {@code WA-TRN-5040}.
 * Maps to HTTP 504. Retriable — transient network condition.
 */
public class WhatsAppTransportTimeoutException extends WhatsAppTransportException {
    public WhatsAppTransportTimeoutException(ApplicationCode code, String message) { super(code, message); }
    public WhatsAppTransportTimeoutException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public WhatsAppTransportTimeoutException(ApplicationCode code) { super(code); }
}
