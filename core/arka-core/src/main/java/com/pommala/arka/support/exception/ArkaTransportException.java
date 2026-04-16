package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a transport-level failure occurs.
 * Base for all channel transport exceptions. Maps to HTTP 502 or 503
 * depending on the internal code. May be retriable per code classification.
 */
public class ArkaTransportException extends ArkaApplicationException {
    public ArkaTransportException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaTransportException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaTransportException(ApplicationCode code) {
        super(code);
    }
}
