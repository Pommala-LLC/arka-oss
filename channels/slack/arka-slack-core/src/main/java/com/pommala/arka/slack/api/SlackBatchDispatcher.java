package com.pommala.arka.slack.api;

import com.pommala.arka.model.BatchDeliveryResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API for fan-out batch Slack dispatch.
 *
 * <p>Each channel ID receives a separate message post.
 */
public interface SlackBatchDispatcher {

    CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> channelIds, Map<String, Object> model);
}
