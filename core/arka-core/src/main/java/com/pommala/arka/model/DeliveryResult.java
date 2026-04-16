package com.pommala.arka.model;

import java.util.Objects;

/**
 * Immutable result of a single channel send operation.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null.</li>
 *   <li>{@code success} — always set; false when error is present.</li>
 *   <li>{@code error} — nullable; null on success, non-null on failure.</li>
 * </ul>
 *
 * @param flowKey the flow key used for this send, never null
 * @param success true if delivered without error
 * @param error   human-readable error description when failed; null on success
 */
public record DeliveryResult(String flowKey, boolean success, String error) {

    public DeliveryResult {
        // Business rule: flowKey is always required.
        Objects.requireNonNull(flowKey, "flowKey must not be null");
    }

    /** Creates a successful delivery result. */
    public static DeliveryResult success(String flowKey) {
        return new DeliveryResult(flowKey, true, null);
    }

    /** Creates a failed delivery result with an error description. */
    public static DeliveryResult failure(String flowKey, String error) {
        return new DeliveryResult(flowKey, false, error);
    }

    /** Creates a failed delivery result from a throwable. */
    public static DeliveryResult failure(String flowKey, Throwable cause) {
        return new DeliveryResult(flowKey, false,
                cause.getClass().getSimpleName() + ": " + cause.getMessage());
    }
}
