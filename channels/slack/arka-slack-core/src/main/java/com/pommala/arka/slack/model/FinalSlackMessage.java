package com.pommala.arka.slack.model;

import java.util.Objects;

/**
 * Immutable send-ready Slack message.
 *
 * @param channelId     target channel or user ID
 * @param text          plain-text message (fallback for Block Kit)
 * @param blocksJson    Block Kit JSON payload (nullable for plain text)
 * @param threadTs      thread timestamp for reply threading (nullable)
 * @param webhookUrl    webhook URL if webhook mode (nullable)
 * @param botToken      bot token if API mode (nullable)
 * @param transportMode "webhook" or "api"
 */
public record FinalSlackMessage(
        String channelId,
        String text,
        String blocksJson,
        String threadTs,
        String webhookUrl,
        String botToken,
        String transportMode) {

    public FinalSlackMessage {
        Objects.requireNonNull(channelId, "channelId must not be null");
        Objects.requireNonNull(transportMode, "transportMode must not be null");
    }

    public boolean isWebhookMode() { return "webhook".equalsIgnoreCase(transportMode); }
    public boolean isApiMode() { return "api".equalsIgnoreCase(transportMode); }
}
