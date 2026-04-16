package com.pommala.arka.teams.model;

import java.util.Objects;

/**
 * Immutable send-ready Teams message.
 *
 * @param channelId       target channel or chat ID
 * @param text            plain-text message body
 * @param adaptiveCardJson Adaptive Card JSON payload (nullable for plain text)
 * @param replyToId       message ID to reply to (nullable)
 * @param webhookUrl      webhook URL if webhook mode (nullable)
 * @param tenantId        Azure tenant ID if graph mode (nullable)
 * @param clientId        Azure client ID if graph mode (nullable)
 * @param clientSecret    Azure client secret if graph mode (nullable)
 * @param transportMode   "webhook" or "graph"
 */
public record FinalTeamsMessage(
        String channelId,
        String text,
        String adaptiveCardJson,
        String replyToId,
        String webhookUrl,
        String tenantId,
        String clientId,
        String clientSecret,
        String transportMode) {

    public FinalTeamsMessage {
        Objects.requireNonNull(channelId, "channelId must not be null");
        Objects.requireNonNull(transportMode, "transportMode must not be null");
    }

    public boolean isWebhookMode() { return "webhook".equalsIgnoreCase(transportMode); }
    public boolean isGraphMode() { return "graph".equalsIgnoreCase(transportMode); }
}
