package com.pommala.arka.email.web.internal;

import java.util.List;
import java.util.Map;

/**
 * HTTP request DTO for email send operations.
 *
 * <p>HTTP shape validation layer: presence and blank checks only.
 * All deeper validation happens downstream in the pipeline.
 */
public record EmailSendRequest(
        String flowKey,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        Map<String, Object> model,
        String subjectOverride) {

    public EmailSendRequest {
        // Null collection normalisation at DTO boundary
        to    = to    != null ? List.copyOf(to)   : List.of();
        cc    = cc    != null ? List.copyOf(cc)   : List.of();
        bcc   = bcc   != null ? List.copyOf(bcc)  : List.of();
        model = model != null ? Map.copyOf(model) : Map.of();
    }
}
