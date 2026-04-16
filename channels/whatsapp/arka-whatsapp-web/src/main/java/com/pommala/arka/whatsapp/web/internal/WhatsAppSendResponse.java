package com.pommala.arka.whatsapp.web.internal;

public record WhatsAppSendResponse(boolean success, String flowKey, String correlationId, String message) {
    public static WhatsAppSendResponse of(String flowKey, String correlationId) {
        return new WhatsAppSendResponse(true, flowKey, correlationId, "WhatsApp message queued for delivery");
    }
}
