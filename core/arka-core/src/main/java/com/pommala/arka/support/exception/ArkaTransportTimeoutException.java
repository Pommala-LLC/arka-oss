package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/**
 * Thrown when a transport operation times out (connect, read, or write).
 * Maps to HTTP 504. Retriable — transient network condition.
 */
public class ArkaTransportTimeoutException extends ArkaTransportException {
    public ArkaTransportTimeoutException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaTransportTimeoutException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaTransportTimeoutException(ApplicationCode code) {
        super(code);
    }
}
