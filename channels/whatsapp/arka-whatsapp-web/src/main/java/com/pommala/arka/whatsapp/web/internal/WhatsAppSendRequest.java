package com.pommala.arka.whatsapp.web.internal;

import java.util.Map;

public record WhatsAppSendRequest(
        String flowKey,
        String to,
        Map<String, Object> model,
        String templateNameOverride,
        String mediaUrl,
        String bodyOverride) {
    public WhatsAppSendRequest {
        model = model != null ? Map.copyOf(model) : Map.of();
    }
}
