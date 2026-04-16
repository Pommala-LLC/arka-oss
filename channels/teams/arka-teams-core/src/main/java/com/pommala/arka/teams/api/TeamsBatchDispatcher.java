package com.pommala.arka.teams.api;

import com.pommala.arka.model.BatchDeliveryResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API for fan-out batch Teams dispatch.
 *
 * <p>Each channel ID receives a separate message.
 */
public interface TeamsBatchDispatcher {

    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> channelIds, Map<String, Object> model);
}
