package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a flow key cannot be resolved by any registered provider.
 * Maps to HTTP 404. Not retriable — the flow key or provider configuration
 * must be corrected.
 */
public class UnknownFlowException extends ArkaApplicationException {
    public UnknownFlowException(ApplicationCode code, String message) {
        super(code, message);
    }
    public UnknownFlowException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public UnknownFlowException(ApplicationCode code) {
        super(code);
    }
}
