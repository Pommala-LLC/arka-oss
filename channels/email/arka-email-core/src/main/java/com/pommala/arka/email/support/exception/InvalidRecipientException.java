package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.email.support.exception.EmailApplicationException;

/**
 * Thrown when a recipient address fails validation and the policy action is FAIL.
 * Maps to HTTP 400. Not retriable — the address must be corrected.
 */
public class InvalidRecipientException extends EmailApplicationException {
    public InvalidRecipientException(ApplicationCode code, String message) {
        super(code, message);
    }
    public InvalidRecipientException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public InvalidRecipientException(ApplicationCode code) {
        super(code);
    }
}
