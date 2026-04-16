package com.pommala.arka.teams.spi;

import com.pommala.arka.spi.ChannelFlowValidator;

/**
 * Teams-specific channel flow validator.
 *
 * <h3>Rules to enforce:</h3>
 * <ul>
 *   <li>Every flow must declare a transport mode (webhook or graph).</li>
 *   <li>Webhook flows must have a webhook-url.</li>
 *   <li>Graph flows must have tenant-id, client-id, client-secret.</li>
 *   <li>A default channel-id or chat-id must be set.</li>
 * </ul>
 */
public interface TeamsFlowValidator extends ChannelFlowValidator {}
