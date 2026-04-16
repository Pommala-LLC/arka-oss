package com.pommala.arka.whatsapp.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaConfigurationException;
import com.pommala.arka.whatsapp.support.code.WhatsAppConfigCategory;
import java.util.Objects;

/**
 * Thrown when a WhatsApp configuration problem prevents delivery.
 * Carries a {@link WhatsAppConfigCategory} for HTTP code mapping. Maps to HTTP 500.
 */
public class WhatsAppConfigurationException extends ArkaConfigurationException {

    private final WhatsAppConfigCategory configCategory;

    public WhatsAppConfigurationException(ApplicationCode code, WhatsAppConfigCategory category, String message) {
        super(code, message);
        this.configCategory = Objects.requireNonNull(category);
    }
    public WhatsAppConfigurationException(ApplicationCode code, WhatsAppConfigCategory category, String message, Throwable cause) {
        super(code, message, cause);
        this.configCategory = Objects.requireNonNull(category);
    }
    public WhatsAppConfigurationException(ApplicationCode code, WhatsAppConfigCategory category) {
        super(code);
        this.configCategory = Objects.requireNonNull(category);
    }

    public WhatsAppConfigCategory configCategory() { return configCategory; }
}
