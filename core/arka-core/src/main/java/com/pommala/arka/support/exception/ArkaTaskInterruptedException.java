package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a virtual thread task is interrupted.
 * Maps to HTTP 503.
 *
 * <p><strong>Rule:</strong> Always call {@code Thread.currentThread().interrupt()}
 * before throwing this exception to restore the interrupt status.</p>
 */
public class ArkaTaskInterruptedException extends ArkaApplicationException {
    public ArkaTaskInterruptedException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaTaskInterruptedException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaTaskInterruptedException(ApplicationCode code) {
        super(code);
    }
}
