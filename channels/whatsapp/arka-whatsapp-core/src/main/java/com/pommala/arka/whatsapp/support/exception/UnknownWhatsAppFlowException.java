package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.UnknownFlowException;

/** Thrown when a WhatsApp flow key cannot be resolved. Maps to HTTP 404. */
public class UnknownWhatsAppFlowException extends UnknownFlowException {
    public UnknownWhatsAppFlowException(ApplicationCode code, String message) { super(code, message); }
    public UnknownWhatsAppFlowException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    public UnknownWhatsAppFlowException(ApplicationCode code) { super(code); }
}
