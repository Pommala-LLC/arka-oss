package com.pommala.arka.sms.api;

import com.pommala.arka.model.BatchDeliveryResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API for fan-out batch SMS dispatch.
 *
 * <p>Privacy rule: each phone number receives a separate SMS.
 */
public interface SmsBatchDispatcher {

    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> phoneNumbers, Map<String, Object> model);
}
