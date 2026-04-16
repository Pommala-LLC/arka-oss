package com.pommala.arka.teams.model;

import java.util.Objects;

/**
 * Immutable resolved Teams flow configuration.
 *
 * @param flowKey         the flow identifier
 * @param template        message template with placeholders
 * @param transportMode   "webhook" or "graph"
 * @param webhookUrl      incoming webhook URL (webhook mode)
 * @param tenantId        Azure AD tenant ID (graph mode)
 * @param clientId        Azure AD client ID (graph mode)
 * @param clientSecret    Azure AD client secret (graph mode)
 * @param defaultChannel  default target channel or chat ID
 * @param messageFormat   "text" or "adaptive-card"
 */
public record ResolvedTeamsFlow(
        String flowKey,
        String template,
        String transportMode,
        String webhookUrl,
        String tenantId,
        String clientId,
        String clientSecret,
        String defaultChannel,
        String messageFormat) {

    public ResolvedTeamsFlow {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        Objects.requireNonNull(transportMode, "transportMode must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        if (transportMode.isBlank()) throw new IllegalArgumentException("transportMode must not be blank");
        messageFormat = messageFormat != null ? messageFormat : "text";
    }

    public boolean isWebhookMode() { return "webhook".equalsIgnoreCase(transportMode); }
    public boolean isGraphMode() { return "graph".equalsIgnoreCase(transportMode); }
}
