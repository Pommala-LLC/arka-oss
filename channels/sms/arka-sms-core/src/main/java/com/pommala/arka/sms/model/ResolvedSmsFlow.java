package com.pommala.arka.sms.model;

import java.util.Objects;

/**
 * Immutable resolved SMS flow configuration.
 *
 * @param flowKey       the flow identifier
 * @param template      template string with placeholders
 * @param fromNumber    sender phone number in E.164 format
 * @param messageType   "transactional" or "promotional"
 */
public record ResolvedSmsFlow(
        String flowKey,
        String template,
        String fromNumber,
        String messageType) {

    public ResolvedSmsFlow {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        Objects.requireNonNull(template, "template must not be null");
        Objects.requireNonNull(fromNumber, "fromNumber must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        if (fromNumber.isBlank()) throw new IllegalArgumentException("fromNumber must not be blank");
        messageType = messageType != null ? messageType : "transactional";
    }
}
