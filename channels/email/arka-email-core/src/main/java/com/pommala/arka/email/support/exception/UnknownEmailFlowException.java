package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.UnknownFlowException;

/**
 * Thrown when an email flow key cannot be resolved by any registered provider.
 * Maps to HTTP 404. Not retriable.
 */
public class UnknownEmailFlowException extends UnknownFlowException {
    public UnknownEmailFlowException(ApplicationCode code, String message) {
        super(code, message);
    }
    public UnknownEmailFlowException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public UnknownEmailFlowException(ApplicationCode code) {
        super(code);
    }
}
