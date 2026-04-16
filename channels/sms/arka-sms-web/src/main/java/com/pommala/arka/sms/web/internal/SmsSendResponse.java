package com.pommala.arka.sms.web.internal;
public record SmsSendResponse(boolean success, String flowKey, String correlationId, String message) {
    public static SmsSendResponse of(String flowKey, String correlationId) {
        return new SmsSendResponse(true, flowKey, correlationId, "SMS queued for delivery");
    }
}
