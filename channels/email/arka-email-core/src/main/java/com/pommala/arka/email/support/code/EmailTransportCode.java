package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * SMTP transport codes. Used by {@code JavaMailEmailTransport} and
 * {@code MailExceptionTranslator}.
 *
 * <p>HTTP mapping rules:</p>
 * <ul>
 *   <li>{@link #SMTP_TIMEOUT} → 504 (via {@code EmailTransportTimeoutException})</li>
 *   <li>{@link #SMTP_SUBSYSTEM_ERROR} → 503 (temporarily unavailable, not a gateway error)</li>
 *   <li>All others → 502 (upstream SMTP failure)</li>
 * </ul>
 *
 * <p>Note on {@link #SMTP_TIMEOUT}: single code covers connect, read, and write
 * timeouts — JavaMail does not reliably distinguish them at the exception chain
 * level.</p>
 *
 * Prefix: {@code EMAIL-TRN-*}
 */
public enum EmailTransportCode implements ApplicationCode {

    SMTP_SEND_FAILED      ("EMAIL-TRN-5020", "SMTP send failed",                false),
    SMTP_SUBSYSTEM_ERROR  ("EMAIL-TRN-5031", "Mail subsystem unavailable",      false),
    SMTP_TIMEOUT          ("EMAIL-TRN-5040", "SMTP timeout",                    true),
    SMTP_AUTH_FAILED      ("EMAIL-TRN-5043", "SMTP auth failed",                false),
    SMTP_TLS_FAILED       ("EMAIL-TRN-5044", "SMTP TLS failed",                 false),
    SMTP_CONNECTION_REFUSED("EMAIL-TRN-5045","SMTP connection refused",         true),
    SMTP_SESSION_INIT_FAILED("EMAIL-TRN-5046","SMTP session init failed",       false),
    SMTP_STALE_CONNECTION ("EMAIL-TRN-5047", "Stale or broken connection",      true);

    private final String code;
    private final String defaultMessage;
    private final boolean retriable;

    EmailTransportCode(String code, String defaultMessage, boolean retriable) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.retriable = retriable;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
    @Override public boolean retriable()     { return retriable; }
}
