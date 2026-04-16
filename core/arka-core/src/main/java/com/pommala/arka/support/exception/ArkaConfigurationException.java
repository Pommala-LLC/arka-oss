package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Thrown when a configuration problem prevents the platform from operating.
 * Maps to HTTP 500. Not retriable — configuration must be corrected.
 */
public class ArkaConfigurationException extends ArkaApplicationException {
    public ArkaConfigurationException(ApplicationCode code, String message) {
        super(code, message);
    }
    public ArkaConfigurationException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public ArkaConfigurationException(ApplicationCode code) {
        super(code);
    }
}
