package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.email.support.exception.EmailApplicationException;

/**
 * Thrown when an email send request is structurally invalid.
 * Maps to HTTP 400. Not retriable.
 */
public class InvalidEmailRequestException extends EmailApplicationException {
    public InvalidEmailRequestException(ApplicationCode code, String message) {
        super(code, message);
    }
    public InvalidEmailRequestException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public InvalidEmailRequestException(ApplicationCode code) {
        super(code);
    }
}
