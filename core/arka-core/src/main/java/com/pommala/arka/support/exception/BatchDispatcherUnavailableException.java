package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when the batch dispatcher's executor rejects task submission.
 * Maps to HTTP 503. Indicates the executor is saturated or shut down.
 */
public class BatchDispatcherUnavailableException extends ArkaApplicationException {
    public BatchDispatcherUnavailableException(ApplicationCode code, String message) {
        super(code, message);
    }
    public BatchDispatcherUnavailableException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public BatchDispatcherUnavailableException(ApplicationCode code) {
        super(code);
    }
}
