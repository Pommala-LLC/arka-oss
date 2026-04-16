package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Thrown when a JavaMail SMTP operation times out.
 *
 * <p>Covers connect, read, and write timeouts — JavaMail does not reliably
 * distinguish them at the exception chain level, so all use
 * {@code EMAIL-TRN-5040}.
 *
 * <p>Extends {@link EmailTransportException} so it is substitutable wherever
 * a transport exception is expected. Maps to HTTP 504. Retriable.
 */
public class EmailTransportTimeoutException extends EmailTransportException {

    public EmailTransportTimeoutException(ApplicationCode code, String message) {
        super(code, message);
    }

    public EmailTransportTimeoutException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public EmailTransportTimeoutException(ApplicationCode code) {
        super(code);
    }
}
