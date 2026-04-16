package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a virtual thread task is cancelled before completion.
 * Maps to HTTP 503.
 */
public class ArkaTaskCancelledException extends ArkaApplicationException {
    public ArkaTaskCancelledException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaTaskCancelledException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaTaskCancelledException(ApplicationCode code) {
        super(code);
    }
}
