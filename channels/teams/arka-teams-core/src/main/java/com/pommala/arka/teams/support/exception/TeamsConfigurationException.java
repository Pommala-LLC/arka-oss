package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaConfigurationException;
import com.pommala.arka.teams.support.code.TeamsConfigCategory;
import java.util.Objects;

/**
 * Thrown when a Teams configuration problem prevents delivery.
 * Carries a {@link TeamsConfigCategory} for HTTP code mapping. Maps to HTTP 500.
 */
public class TeamsConfigurationException extends ArkaConfigurationException {

    private final TeamsConfigCategory configCategory;

    public TeamsConfigurationException(ApplicationCode code, TeamsConfigCategory category, String message) {
        super(code, message);
        this.configCategory = Objects.requireNonNull(category);
    }
    public TeamsConfigurationException(ApplicationCode code, TeamsConfigCategory category, String message, Throwable cause) {
        super(code, message, cause);
        this.configCategory = Objects.requireNonNull(category);
    }
    public TeamsConfigurationException(ApplicationCode code, TeamsConfigCategory category) {
        super(code);
        this.configCategory = Objects.requireNonNull(category);
    }

    public TeamsConfigCategory configCategory() { return configCategory; }
}
