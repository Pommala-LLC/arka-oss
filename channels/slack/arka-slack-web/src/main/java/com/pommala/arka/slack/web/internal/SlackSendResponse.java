package com.pommala.arka.slack.web.internal;
public record SlackSendResponse(boolean success, String flowKey, String correlationId, String message) {
    public static SlackSendResponse of(String flowKey, String correlationId) {
        return new SlackSendResponse(true, flowKey, correlationId, "Slack message queued for delivery");
    }
}
