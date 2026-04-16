package com.pommala.arka.whatsapp.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable send-ready WhatsApp message.
 *
 * @param to              destination phone number in E.164 format
 * @param phoneNumberId   the Meta phone-number-id for sending
 * @param accessToken     the Meta access token
 * @param deliveryMode    "session" or "template"
 * @param templateName    Meta template name (template mode)
 * @param languageCode    template language code (template mode)
 * @param templateParams  ordered list of template parameter values (template mode)
 * @param body            message body text (session mode)
 * @param mediaUrl        optional media attachment URL
 * @param metadata        additional transport metadata, never null
 */
public record FinalWhatsAppMessage(
        String to,
        String phoneNumberId,
        String accessToken,
        String deliveryMode,
        String templateName,
        String languageCode,
        List<String> templateParams,
        String body,
        String mediaUrl,
        Map<String, String> metadata) {

    public FinalWhatsAppMessage {
        Objects.requireNonNull(to, "to must not be null");
        Objects.requireNonNull(phoneNumberId, "phoneNumberId must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        templateParams = templateParams != null ? List.copyOf(templateParams) : List.of();
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
    }

    public boolean isTemplateMode() { return "template".equalsIgnoreCase(deliveryMode); }
    public boolean isSessionMode() { return "session".equalsIgnoreCase(deliveryMode); }
}
