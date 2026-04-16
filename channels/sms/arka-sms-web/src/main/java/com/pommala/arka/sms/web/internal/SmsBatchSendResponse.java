package com.pommala.arka.sms.web.internal;
import com.pommala.arka.model.BatchDeliveryResult;
public record SmsBatchSendResponse(boolean success, String flowKey, String requestId,
        int totalRecipients, long successCount, long failureCount, boolean allSucceeded) {
    public static SmsBatchSendResponse of(BatchDeliveryResult result, String requestId) {
        return new SmsBatchSendResponse(true, result.flowKey(), requestId,
                result.totalRecipients(), result.successCount(), result.failureCount(), result.allSucceeded());
    }
}
