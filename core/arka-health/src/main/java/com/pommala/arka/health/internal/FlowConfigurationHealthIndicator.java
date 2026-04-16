package com.pommala.arka.health.internal;

import com.pommala.arka.spi.ChannelFlowValidator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Verifies flow configuration validity across all channels.
 *
 * <p>No actuator dependency — this is a plain bean. The autoconfig
 * adapts it to a {@code HealthIndicator} only when Actuator is present.
 */
public class FlowConfigurationHealthIndicator {

    private final List<ChannelFlowValidator> validators;

    public FlowConfigurationHealthIndicator(List<ChannelFlowValidator> validators) {
        this.validators = Objects.requireNonNull(validators);
    }

    /** Returns health status as a map: {status, channelsValidated, ...per-channel details}. */
    public Map<String, Object> check() {
        var details = new LinkedHashMap<String, Object>();
        var totalErrors = 0;

        for (var validator : validators) {
            var errors = validator.validate();
            details.put("channel." + validator.channelId() + ".flows", "validated");
            if (!errors.isEmpty()) {
                totalErrors += errors.size();
                details.put("channel." + validator.channelId() + ".errors", errors.size());
            }
        }

        details.put("channelsValidated", validators.size());
        details.put("totalValidationErrors", totalErrors);
        details.put("status", totalErrors > 0 ? "DOWN" : "UP");
        return Map.copyOf(details);
    }
}
