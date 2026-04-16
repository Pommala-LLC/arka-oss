package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Abstract base for all email channel exceptions.
 * Extends {@link ArkaApplicationException} — carries an {@link ApplicationCode}.
 * All concrete email exceptions extend this class.
 */
public abstract class EmailApplicationException extends ArkaApplicationException {

    protected EmailApplicationException(ApplicationCode code, String message) {
        super(code, message);
    }

    protected EmailApplicationException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    protected EmailApplicationException(ApplicationCode code) {
        super(code);
    }
}
