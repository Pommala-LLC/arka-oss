package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/** Abstract base for all WhatsApp channel exceptions. */
public abstract class WhatsAppApplicationException extends ArkaApplicationException {
    protected WhatsAppApplicationException(ApplicationCode code, String message) { super(code, message); }
    protected WhatsAppApplicationException(ApplicationCode code, String message, Throwable cause) { super(code, message, cause); }
    protected WhatsAppApplicationException(ApplicationCode code) { super(code); }
}
