package com.pommala.arka.email.web.internal;

import java.util.List;
import java.util.Map;

/**
 * HTTP request DTO for batch email send operations.
 *
 * @param flowKey   the flow key, never null or blank
 * @param addresses recipient addresses; each gets an individual email
 * @param model     shared template variables for all recipients
 */
public record BatchSendRequest(
        String flowKey,
        List<String> addresses,
        Map<String, Object> model) {

    public BatchSendRequest {
        addresses = addresses != null ? List.copyOf(addresses) : List.of();
        model     = model     != null ? Map.copyOf(model)      : Map.of();
    }
}
