package com.pommala.arka.teams.web.internal;
import com.pommala.arka.model.BatchDeliveryResult;
public record TeamsBatchSendResponse(boolean success, String flowKey, String requestId,
        int totalRecipients, long successCount, long failureCount, boolean allSucceeded) {
    public static TeamsBatchSendResponse of(BatchDeliveryResult result, String requestId) {
        return new TeamsBatchSendResponse(true, result.flowKey(), requestId,
                result.totalRecipients(), result.successCount(), result.failureCount(), result.allSucceeded());
    }
}
