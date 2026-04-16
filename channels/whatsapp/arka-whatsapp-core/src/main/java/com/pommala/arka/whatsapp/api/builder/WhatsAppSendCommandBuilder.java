package com.pommala.arka.whatsapp.api.builder;

import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Fluent builder for {@link WhatsAppSendCommand}.
 *
 * <p>Multiple calls to {@link #model(String, Object)} accumulate — they do not replace.
 *
 * @see WhatsAppSendCommand
 */
public final class WhatsAppSendCommandBuilder {

    private final String flowKey;
    private String to;
    private final Map<String, Object> model = new HashMap<>();
    private String templateNameOverride;
    private String mediaUrl;
    private String bodyOverride;

    public WhatsAppSendCommandBuilder(String flowKey) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        if (flowKey.isBlank()) throw new IllegalArgumentException("flowKey must not be blank");
        this.flowKey = flowKey;
    }

    /** Sets the destination phone number in E.164 format. Required. */
    public WhatsAppSendCommandBuilder to(String phoneNumber) {
        this.to = phoneNumber;
        return this;
    }

    /** Adds a single template variable. Accumulates across calls. */
    public WhatsAppSendCommandBuilder model(String key, Object value) {
        model.put(key, value);
        return this;
    }

    /** Adds all entries from the given map. Accumulates across calls. */
    public WhatsAppSendCommandBuilder model(Map<String, Object> variables) {
        model.putAll(variables);
        return this;
    }

    /** Overrides the flow-level Meta template name. Optional. */
    public WhatsAppSendCommandBuilder templateNameOverride(String templateName) {
        this.templateNameOverride = templateName;
        return this;
    }

    /** Sets the media attachment URL for media messages. Optional. */
    public WhatsAppSendCommandBuilder mediaUrl(String url) {
        this.mediaUrl = url;
        return this;
    }

    /** Overrides the template body for session messages. Optional. */
    public WhatsAppSendCommandBuilder bodyOverride(String body) {
        this.bodyOverride = body;
        return this;
    }

    public WhatsAppSendCommand build() {
        return new WhatsAppSendCommand(flowKey, to, Map.copyOf(model),
                templateNameOverride, mediaUrl, bodyOverride);
    }
}
