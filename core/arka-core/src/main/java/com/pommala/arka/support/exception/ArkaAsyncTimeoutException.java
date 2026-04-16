package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when an async dispatch operation exceeds its configured deadline.
 * Maps to HTTP 504. Channel async modules extend this class.
 */
public class ArkaAsyncTimeoutException extends ArkaApplicationException {
    public ArkaAsyncTimeoutException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaAsyncTimeoutException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaAsyncTimeoutException(ApplicationCode code) {
        super(code);
    }
}
