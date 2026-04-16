package com.pommala.arka.model;

import java.util.List;
import java.util.Objects;

/**
 * Immutable aggregated result of a batch send operation.
 *
 * <p>All collection fields use {@code List.copyOf()}.
 * This record is fully immutable after construction.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null.</li>
 *   <li>{@code results} — required, non-null, defensively copied.</li>
 * </ul>
 *
 * @param flowKey the flow key used for this batch, never null
 * @param results per-destination outcomes, never null, immutable after construction
 */
public record BatchDeliveryResult(String flowKey, List<DeliveryResult> results) {

    public BatchDeliveryResult {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        // Immutability rule: all collection fields defensively copied.
        results = results != null ? List.copyOf(results) : List.of();
    }

    /** Returns true if all results succeeded and there is at least one. */
    public boolean allSucceeded() {
        return !results.isEmpty() && results.stream().allMatch(DeliveryResult::success);
    }

    /** Returns true if at least one result failed. */
    public boolean hasFailures() {
        return results.stream().anyMatch(r -> !r.success());
    }

    /** Count of successful deliveries. */
    public long successCount() {
        return results.stream().filter(DeliveryResult::success).count();
    }

    /** Count of failed deliveries. */
    public long failureCount() {
        return results.stream().filter(r -> !r.success()).count();
    }

    /** Total number of recipients in this batch. */
    public int totalRecipients() {
        return results.size();
    }

    /** Successful results only. */
    public List<DeliveryResult> successes() {
        return results.stream().filter(DeliveryResult::success).toList();
    }

    /** Failed results only. */
    public List<DeliveryResult> failures() {
        return results.stream().filter(r -> !r.success()).toList();
    }
}
