package com.pommala.arka.email.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/**
 * Thrown when a JavaMail SMTP transport operation fails.
 *
 * <p>HTTP mapping is code-based — the exception handler inspects the internal
 * code rather than the exception type alone:</p>
 * <ul>
 *   <li>{@code EMAIL-TRN-5031} → 503 (subsystem temporarily unavailable)</li>
 *   <li>{@code EMAIL-TRN-5040} → 504 (via {@link EmailTransportTimeoutException})</li>
 *   <li>All other {@code EMAIL-TRN-*} → 502 (upstream SMTP failure)</li>
 * </ul>
 *
 * <p>All low-level exceptions ({@code MessagingException}, {@code MailException},
 * etc.) must be translated to this type or its subtypes by
 * {@code MailExceptionTranslator} before leaving the transport layer.
 * Raw framework exceptions must never escape.</p>
 */
public class EmailTransportException extends ArkaTransportException {
    public EmailTransportException(ApplicationCode code, String message) {
        super(code, message);
    }
    public EmailTransportException(ApplicationCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public EmailTransportException(ApplicationCode code) {
        super(code);
    }
}
