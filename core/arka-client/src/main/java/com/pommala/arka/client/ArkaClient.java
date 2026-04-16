package com.pommala.arka.client;

import java.util.List;
import java.util.Map;

/**
 * Typed client for calling the Arka REST API from another service.
 *
 * <p>Provides a channel-agnostic interface for single and batch send
 * operations. Consumers use this to submit messages without importing
 * channel-specific modules.
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * arkaClient.send("email", Map.of(
 *     "flowKey", "welcome",
 *     "to", List.of("user@example.com"),
 *     "model", Map.of("name", "Alice")));
 * }</pre>
 */
public interface ArkaClient {

    /**
     * Sends a single message via the specified channel.
     *
     * @param channel the channel name (email, whatsapp, sms, slack, teams)
     * @param request the send request payload as a map
     * @return response payload from the Arka REST API
     */
    Map<String, Object> send(String channel, Map<String, Object> request);

    /**
     * Sends a batch of messages via the specified channel.
     *
     * @param channel the channel name
     * @param request the batch request payload as a map
     * @return response payload from the Arka REST API
     */
    Map<String, Object> batch(String channel, Map<String, Object> request);
}
