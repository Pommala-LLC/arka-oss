package com.pommala.arka.whatsapp.web.internal;

import com.pommala.arka.model.BatchDeliveryResult;

public record WhatsAppBatchSendResponse(boolean success, String flowKey, String requestId,
        int totalRecipients, long successCount, long failureCount, boolean allSucceeded) {
    public static WhatsAppBatchSendResponse of(BatchDeliveryResult result, String requestId) {
        return new WhatsAppBatchSendResponse(true, result.flowKey(), requestId,
                result.totalRecipients(), result.successCount(), result.failureCount(), result.allSucceeded());
    }
}
