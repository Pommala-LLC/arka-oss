package com.pommala.arka.whatsapp.model;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable resolved WhatsApp flow configuration.
 *
 * @param flowKey         the flow identifier
 * @param deliveryMode    "session" or "template"
 * @param phoneNumberId   the Meta phone-number-id for sending
 * @param accessToken     the Meta access token (resolved from sender-ref)
 * @param templateName    Meta-approved template name (template mode only)
 * @param languageCode    template language code (template mode only)
 * @param defaultBody     default body text (session mode only)
 * @param mediaConfig     media attachment configuration, may be empty
 */
public record ResolvedWhatsAppFlow(
        String flowKey,
        String deliveryMode,
        String phoneNumberId,
        String accessToken,
        String templateName,
        String languageCode,
        String defaultBody,
        Map<String, String> mediaConfig) {

    public ResolvedWhatsAppFlow {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(phoneNumberId, "phoneNumberId must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        if (deliveryMode.isBlank()) throw new IllegalArgumentException("deliveryMode must not be blank");
        if (phoneNumberId.isBlank()) throw new IllegalArgumentException("phoneNumberId must not be blank");
        mediaConfig = mediaConfig != null ? Map.copyOf(mediaConfig) : Map.of();
    }

    public boolean isTemplateMode() { return "template".equalsIgnoreCase(deliveryMode); }
    public boolean isSessionMode() { return "session".equalsIgnoreCase(deliveryMode); }
}
