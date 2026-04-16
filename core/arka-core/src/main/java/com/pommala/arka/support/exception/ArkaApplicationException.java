package com.pommala.arka.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import java.util.Objects;

/**
 * Abstract base for all Arka platform exceptions.
 *
 * <p>Every exception in the platform carries an {@link ApplicationCode} that
 * drives logs, metrics, and API error responses. The code is mandatory —
 * construction fails fast with {@link NullPointerException} if absent.</p>
 *
 * <p>Three-constructor discipline is mandatory for all concrete subtypes:</p>
 * <ol>
 *   <li>{@code (Code code, String message)}</li>
 *   <li>{@code (Code code, String message, Throwable cause)}</li>
 *   <li>{@code (Code code)} — uses {@link ApplicationCode#defaultMessage()}</li>
 * </ol>
 */
public abstract class ArkaApplicationException extends RuntimeException {

    private final ApplicationCode internalCode;

    protected ArkaApplicationException(ApplicationCode code, String message) {
        super(message);
        this.internalCode = Objects.requireNonNull(code, "internalCode must not be null");
    }

    protected ArkaApplicationException(ApplicationCode code, String message, Throwable cause) {
        super(message, cause);
        this.internalCode = Objects.requireNonNull(code, "internalCode must not be null");
    }

    protected ArkaApplicationException(ApplicationCode code) {
        super(code.defaultMessage());
        this.internalCode = Objects.requireNonNull(code, "internalCode must not be null");
    }

    /** The internal application code driving logs, metrics, and error responses. */
    public ApplicationCode internalCode() { return internalCode; }

    /** Whether the failure with this code is eligible for retry. */
    public boolean retriable() { return internalCode.retriable(); }
}
