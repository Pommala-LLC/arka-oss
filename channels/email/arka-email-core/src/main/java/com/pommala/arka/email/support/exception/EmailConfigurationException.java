package com.pommala.arka.email.support.exception;

import com.pommala.arka.email.support.code.EmailConfigCategory;
import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaConfigurationException;
import java.util.Objects;

/**
 * Thrown when an email configuration problem prevents delivery.
 * Carries an {@link EmailConfigCategory} for exhaustive HTTP code mapping.
 *
 * <p>The exception handler maps category to HTTP code:</p>
 * <pre>
 *   FLOW       → EMAIL-HTTP-5004 (500)
 *   SENDER     → EMAIL-HTTP-5002 (500)
 *   ATTACHMENT → EMAIL-HTTP-5003 (500)
 *   TEMPLATE   → EMAIL-HTTP-5005 (500)
 *   MESSAGE    → EMAIL-HTTP-5005 (500)
 * </pre>
 *
 * Maps to HTTP 500. Not retriable.
 */
public class EmailConfigurationException extends ArkaConfigurationException {

    private final EmailConfigCategory configCategory;

    public EmailConfigurationException(ApplicationCode code, EmailConfigCategory category,
                                       String message) {
        super(code, message);
        this.configCategory = Objects.requireNonNull(category, "configCategory must not be null");
    }

    public EmailConfigurationException(ApplicationCode code, EmailConfigCategory category,
                                       String message, Throwable cause) {
        super(code, message, cause);
        this.configCategory = Objects.requireNonNull(category, "configCategory must not be null");
    }

    public EmailConfigurationException(ApplicationCode code, EmailConfigCategory category) {
        super(code);
        this.configCategory = Objects.requireNonNull(category, "configCategory must not be null");
    }

    public EmailConfigCategory configCategory() { return configCategory; }
}
