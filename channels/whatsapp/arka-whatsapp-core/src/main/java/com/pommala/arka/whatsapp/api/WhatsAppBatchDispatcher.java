package com.pommala.arka.whatsapp.api;

import com.pommala.arka.model.BatchDeliveryResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API for fan-out batch WhatsApp dispatch.
 *
 * <p>Contract type: {@code api} — application code calls this directly.
 *
 * <h3>Privacy rule (locked):</h3>
 * Each phone number receives a separate message. The dispatcher never batches
 * multiple destinations into a single API call.
 */
public interface WhatsAppBatchDispatcher {

    /**
     * Dispatches one WhatsApp message per phone number, all using the same template model.
     *
     * @param flowKey      the flow key, never null or blank
     * @param phoneNumbers destination phone numbers in E.164, never null or empty
     * @param model        shared template variables, never null
     * @return future resolving to the aggregated batch result
     */
    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> phoneNumbers, Map<String, Object> model);
}
