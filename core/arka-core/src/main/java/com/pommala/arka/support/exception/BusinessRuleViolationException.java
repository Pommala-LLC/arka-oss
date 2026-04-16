package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a send request violates a platform business rule.
 * Canonical example: no valid primary destination after normalization.
 * Maps to HTTP 422. Not retriable — the request must be corrected by the caller.
 */
public class BusinessRuleViolationException extends ArkaApplicationException {
    public BusinessRuleViolationException(ApplicationCode code, String message) {
        super(code, message);
    }
    public BusinessRuleViolationException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public BusinessRuleViolationException(ApplicationCode code) {
        super(code);
    }
}
