package com.pommala.arka.slack.web.internal;
import com.pommala.arka.model.BatchDeliveryResult;
public record SlackBatchSendResponse(boolean success, String flowKey, String requestId,
        int totalRecipients, long successCount, long failureCount, boolean allSucceeded) {
    public static SlackBatchSendResponse of(BatchDeliveryResult result, String requestId) {
        return new SlackBatchSendResponse(true, result.flowKey(), requestId,
                result.totalRecipients(), result.successCount(), result.failureCount(), result.allSucceeded());
    }
}
