package com.pommala.arka.email.web.internal;

import com.pommala.arka.model.BatchDeliveryResult;

/**
 * HTTP response DTO for batch email send operations.
 *
 * @param success        always true for success responses
 * @param flowKey        the flow key used
 * @param requestId      the tracking ID for this batch request
 * @param totalRecipients total number of recipients in the batch
 * @param successCount   number of successful deliveries
 * @param failureCount   number of failed deliveries
 * @param allSucceeded   true if all recipients received successfully
 */
public record BatchSendResponse(
        boolean success,
        String flowKey,
        String requestId,
        int totalRecipients,
        long successCount,
        long failureCount,
        boolean allSucceeded) {

    /** Creates a standard batch response from a {@link BatchDeliveryResult}. */
    public static BatchSendResponse of(BatchDeliveryResult result, String requestId) {
        return new BatchSendResponse(
                true,
                result.flowKey(),
                requestId,
                result.totalRecipients(),
                result.successCount(),
                result.failureCount(),
                result.allSucceeded());
    }
}
