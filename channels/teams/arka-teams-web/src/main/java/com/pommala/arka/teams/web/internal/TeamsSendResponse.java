package com.pommala.arka.teams.web.internal;
public record TeamsSendResponse(boolean success, String flowKey, String correlationId, String message) {
    public static TeamsSendResponse of(String flowKey, String correlationId) {
        return new TeamsSendResponse(true, flowKey, correlationId, "Teams message queued for delivery");
    }
}
