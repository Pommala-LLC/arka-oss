package com.pommala.arka.slack.spi;

import com.pommala.arka.spi.ChannelFlowValidator;

/**
 * Slack-specific channel flow validator.
 *
 * <h3>Rules to enforce:</h3>
 * <ul>
 *   <li>Every flow must declare a transport mode (webhook or api).</li>
 *   <li>Webhook flows must have a webhook-ref resolving to a URL.</li>
 *   <li>API flows must have a bot-token-ref.</li>
 *   <li>A default channel-id must be set on every flow.</li>
 * </ul>
 */
public interface SlackFlowValidator extends ChannelFlowValidator {}
