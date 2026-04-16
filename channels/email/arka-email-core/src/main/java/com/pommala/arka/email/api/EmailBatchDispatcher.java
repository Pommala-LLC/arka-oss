package com.pommala.arka.email.api;

import com.pommala.arka.model.BatchDeliveryResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API for fan-out batch email dispatch.
 *
 * <p>Contract type: {@code api} — application code calls this directly.
 *
 * <h3>Privacy rule (locked):</h3>
 * Each recipient receives a separate email. The dispatcher never puts multiple
 * recipients in a single {@code to} field. No recipient can see other addresses.
 *
 * @see com.pommala.arka.model.BatchDeliveryResult
 */
public interface EmailBatchDispatcher {

    /**
     * Dispatches one email per address in the list, all using the same template model.
     *
     * @param flowKey   the flow key, never null or blank
     * @param addresses recipient address strings, never null or empty
     * @param model     shared template variables, never null
     * @return future resolving to the aggregated batch result
     */
    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> addresses, Map<String, Object> model);

    /**
     * Dispatches one email per entry in the map, with per-recipient template models.
     *
     * @param flowKey        the flow key, never null or blank
     * @param modelByAddress map of address to its own template variables
     * @return future resolving to the aggregated batch result
     */
    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, Map<String, Map<String, Object>> modelByAddress);
}
