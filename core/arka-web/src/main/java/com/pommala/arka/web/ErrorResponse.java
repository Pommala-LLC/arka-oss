package com.pommala.arka.web;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Standard error response shape for all Arka HTTP surfaces.
 *
 * <p>All collection fields use {@code List.copyOf()}.
 * This record is fully immutable after construction.
 *
 * @param success      always false for error responses
 * @param code         external/API-facing HTTP code string
 * @param httpStatus   numeric HTTP status
 * @param message      human-readable error description
 * @param internalCode internal application code for support diagnostics
 * @param details      typed field-level error details, never null
 * @param path         request path
 * @param timestamp    ISO 8601 timestamp
 */
public record ErrorResponse(
        boolean success,
        String code,
        int httpStatus,
        String message,
        String internalCode,
        List<FieldErrorDetail> details,
        String path,
        String timestamp) {

    public ErrorResponse {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(message, "message must not be null");
        // Immutability rule: collection fields defensively copied.
        details = details != null ? List.copyOf(details) : List.of();
        timestamp = timestamp != null ? timestamp : Instant.now().toString();
    }

    /**
     * Typed field-level error detail.
     *
     * @param field         the affected field name
     * @param rejectedValue the value that was rejected, nullable
     * @param message       field-specific error description
     */
    public record FieldErrorDetail(String field, String rejectedValue, String message) {}
}
