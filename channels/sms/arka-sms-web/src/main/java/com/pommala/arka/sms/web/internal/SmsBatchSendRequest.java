package com.pommala.arka.sms.web.internal;
import java.util.List;
import java.util.Map;
public record SmsBatchSendRequest(String flowKey, List<String> phoneNumbers, Map<String, Object> model) {
    public SmsBatchSendRequest {
        phoneNumbers = phoneNumbers != null ? List.copyOf(phoneNumbers) : List.of();
        model = model != null ? Map.copyOf(model) : Map.of();
    }
}
