package com.pommala.arka.slack.model;

import java.util.Objects;

/**
 * Immutable resolved Slack flow configuration.
 *
 * @param flowKey        the flow identifier
 * @param template       message template with placeholders
 * @param transportMode  "webhook" or "api"
 * @param webhookUrl     incoming webhook URL (webhook mode)
 * @param botToken       bot OAuth token (api mode)
 * @param defaultChannel default target channel ID
 * @param messageFormat  "text" or "blocks" (Block Kit)
 */
public record ResolvedSlackFlow(
        String flowKey,
        String template,
        String transportMode,
        String webhookUrl,
        String botToken,
        String defaultChannel,
        String messageFormat) {

    public ResolvedSlackFlow {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        Objects.requireNonNull(transportMode, "transportMode must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        if (transportMode.isBlank()) throw new IllegalArgumentException("transportMode must not be blank");
        messageFormat = messageFormat != null ? messageFormat : "text";
    }

    public boolean isWebhookMode() { return "webhook".equalsIgnoreCase(transportMode); }
    public boolean isApiMode() { return "api".equalsIgnoreCase(transportMode); }
}
