package com.pommala.arka.spi;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;

/**
 * SPI for channel-specific send pipelines.
 *
 * <p>Contract type: {@code spi} — framework extension point.
 * Each channel registers exactly one handler bean.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: implementations must be thread-safe.</li>
 *   <li>Registration: Spring bean. One bean per channel ID.</li>
 *   <li>The handler owns: flow resolution, destination normalization,
 *       message construction, circuit-breaker check, retry loop, and transport send.</li>
 *   <li>Interceptors are called by the shared service — handlers must not call them.</li>
 * </ul>
 */
public interface ChannelSendHandler {

    /**
     * Returns the channel identifier this handler serves.
     * Must be stable and unique across all registered handlers.
     */
    String channelId();

    /**
     * Executes the full channel send pipeline for the given command.
     *
     * @param command the typed send command, never null
     * @return delivery result, never null
     */
    DeliveryResult handle(SendCommand command);
}
