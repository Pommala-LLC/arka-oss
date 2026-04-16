package com.pommala.arka.health.internal;

import com.pommala.arka.spi.ChannelSendHandler;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Checks that at least one channel send handler is registered.
 *
 * <p>No actuator dependency — this is a plain bean. The autoconfig
 * adapts it to a {@code HealthIndicator} only when Actuator is present.
 */
public class ChannelRegistrationHealthIndicator {

    private final List<ChannelSendHandler> handlers;

    public ChannelRegistrationHealthIndicator(List<ChannelSendHandler> handlers) {
        this.handlers = Objects.requireNonNull(handlers);
    }

    /** Returns health status as a map: {status, registeredChannels, channels}. */
    public Map<String, Object> check() {
        if (handlers.isEmpty()) {
            return Map.of(
                    "status", "DOWN",
                    "reason", "No ChannelSendHandler beans registered",
                    "registeredChannels", 0);
        }
        var channelIds = handlers.stream()
                .map(ChannelSendHandler::channelId)
                .toList();
        return Map.of(
                "status", "UP",
                "registeredChannels", channelIds.size(),
                "channels", channelIds);
    }
}
