package com.pommala.arka.email.web.internal;

/**
 * HTTP response DTO for successful email send operations.
 *
 * @param success       always true for success responses
 * @param flowKey       the flow key used for this send
 * @param correlationId the tracking ID assigned to this send
 * @param message       human-readable confirmation
 */
public record EmailSendResponse(
        boolean success,
        String flowKey,
        String correlationId,
        String message) {

    /** Creates a standard success response. */
    public static EmailSendResponse of(String flowKey, String correlationId) {
        return new EmailSendResponse(true, flowKey, correlationId, "Email queued for delivery");
    }
}
