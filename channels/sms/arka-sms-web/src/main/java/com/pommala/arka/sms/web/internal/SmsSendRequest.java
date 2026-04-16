package com.pommala.arka.sms.web.internal;
import java.util.Map;
public record SmsSendRequest(String flowKey, String to, Map<String, Object> model, String bodyOverride, String fromOverride) {
    public SmsSendRequest { model = model != null ? Map.copyOf(model) : Map.of(); }
}
