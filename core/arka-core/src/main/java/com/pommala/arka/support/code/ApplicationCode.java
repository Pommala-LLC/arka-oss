package com.pommala.arka.support.code;

/**
 * Marker interface for all application codes across the Arka platform.
 *
 * <p>Every code enum in the platform implements this interface. Codes drive:
 * logs, exceptions, metrics, and support diagnostics. HTTP-facing codes in the
 * web layer map from these internal codes — they are never exposed directly.</p>
 *
 * <p><strong>Hard rule:</strong> No class implementing this interface may import
 * anything outside {@code java.*}. Codes live in core and core has zero external
 * runtime dependencies.</p>
 */
public interface ApplicationCode {

    /** Unique code string, e.g. {@code "EMAIL-TRN-5040"}, {@code "WA-VAL-4001"}. */
    String code();

    /** Human-readable default message. Used when no override is provided. */
    String defaultMessage();

    /**
     * Whether a failure with this code is eligible for retry.
     * Defaults to {@code false} — override in transport and async code enums.
     */
    default boolean retriable() { return false; }
}
