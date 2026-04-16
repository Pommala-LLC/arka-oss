package com.pommala.arka.sms.model;

import java.util.Objects;

/**
 * Immutable send-ready SMS message.
 *
 * @param to            destination phone number in E.164 format
 * @param from          sender phone number in E.164 format
 * @param body          resolved message body text
 * @param messageType   "transactional" or "promotional"
 */
public record FinalSmsMessage(
        String to,
        String from,
        String body,
        String messageType) {

    public FinalSmsMessage {
        Objects.requireNonNull(to, "to must not be null");
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(body, "body must not be null");
        messageType = messageType != null ? messageType : "transactional";
    }
}
