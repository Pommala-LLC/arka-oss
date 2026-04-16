package com.pommala.arka.spi;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.support.exception.ArkaApplicationException;
import java.time.Duration;

/**
 * Additive SPI for pre/post send lifecycle hooks.
 *
 * <p>Contract type: {@code spi} — all registered beans are called in
 * registration order. Multiple beans are supported.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: implementations must be thread-safe.</li>
 *   <li>Interceptors must NEVER throw — swallow and log all internal exceptions.
 *       A broken interceptor must never abort a send operation.</li>
 *   <li>Interceptors must NEVER modify the command or result objects.</li>
 * </ul>
 */
public interface SendInterceptor {

    /**
     * Called before the send pipeline begins.
     *
     * @param command the send command, never null
     */
    default void beforeSend(SendCommand command) {}

    /**
     * Called after successful delivery.
     *
     * @param command the send command, never null
     * @param elapsed time taken for the operation
     * @param result  the delivery result, never null
     */
    default void afterSuccess(SendCommand command, Duration elapsed, DeliveryResult result) {}

    /**
     * Called after a failed delivery.
     *
     * @param command the send command, never null
     * @param elapsed time taken before failure
     * @param ex      the typed exception that caused the failure
     */
    default void afterFailure(SendCommand command, Duration elapsed, ArkaApplicationException ex) {}
}
