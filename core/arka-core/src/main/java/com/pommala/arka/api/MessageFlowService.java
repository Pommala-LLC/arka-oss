package com.pommala.arka.api;

import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.support.exception.ArkaApplicationException;

/**
 * Primary API for submitting a single send operation to the Arka engine.
 *
 * <p>Contract type: {@code api} — application code calls this directly.
 *
 * <h3>Behavioural contract:</h3>
 * <ul>
 *   <li>Routes the command to the registered {@code ChannelSendHandler} for the channel.</li>
 *   <li>Calls {@code SendInterceptor.beforeSend()} before dispatch and
 *       {@code afterSuccess()} / {@code afterFailure()} after.</li>
 *   <li>Throws a typed {@link ArkaApplicationException} subtype — never a raw exception.</li>
 * </ul>
 *
 * @see SendCommand
 * @see DeliveryResult
 */
public interface MessageFlowService {

    /**
     * Sends a single message via the channel identified by {@code command}.
     *
     * @param command the send command, never null
     * @return the delivery result, never null
     * @throws ArkaApplicationException on any failure — typed subtype carries the code
     * @throws NullPointerException     if command is null
     */
    DeliveryResult send(SendCommand command);
}
