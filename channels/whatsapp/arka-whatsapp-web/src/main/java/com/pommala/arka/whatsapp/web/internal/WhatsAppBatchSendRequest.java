package com.pommala.arka.whatsapp.web.internal;

import java.util.List;
import java.util.Map;

public record WhatsAppBatchSendRequest(String flowKey, List<String> phoneNumbers, Map<String, Object> model) {
    public WhatsAppBatchSendRequest {
        phoneNumbers = phoneNumbers != null ? List.copyOf(phoneNumbers) : List.of();
        model = model != null ? Map.copyOf(model) : Map.of();
    }
}
