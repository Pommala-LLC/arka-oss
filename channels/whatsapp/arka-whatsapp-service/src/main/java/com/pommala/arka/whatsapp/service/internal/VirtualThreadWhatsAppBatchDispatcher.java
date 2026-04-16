package com.pommala.arka.whatsapp.service.internal;

import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.async.internal.VirtualThreadExecutorFactory;
import com.pommala.arka.model.BatchDeliveryResult;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.support.code.ArkaCoreCode;
import com.pommala.arka.support.exception.ArkaTaskInterruptedException;
import com.pommala.arka.support.exception.BatchDispatcherUnavailableException;
import com.pommala.arka.whatsapp.api.WhatsAppBatchDispatcher;
import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Virtual-thread based WhatsApp batch dispatcher.
 *
 * <p>Fans out one {@link WhatsAppSendCommand} per phone number using Java 21
 * virtual threads. Each number receives a separate message.
 */
public class VirtualThreadWhatsAppBatchDispatcher implements WhatsAppBatchDispatcher {

    private static final String LOG_PREFIX = "[arka-whatsapp-batch]";
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadWhatsAppBatchDispatcher.class);

    private final DefaultWhatsAppSendHandler sendHandler;
    private final ExecutionContextPropagatorChain propagatorChain;
    private final ThrottlePolicy throttle;
    private final Duration batchTimeout;
    private final Duration recipientTimeout;
    private final ExecutorService executor;

    public VirtualThreadWhatsAppBatchDispatcher(DefaultWhatsAppSendHandler sendHandler,
                                                 ExecutionContextPropagatorChain propagatorChain,
                                                 ThrottlePolicy throttle,
                                                 Duration batchTimeout,
                                                 Duration recipientTimeout) {
        this.sendHandler      = Objects.requireNonNull(sendHandler);
        this.propagatorChain  = Objects.requireNonNull(propagatorChain);
        this.throttle         = Objects.requireNonNull(throttle);
        this.batchTimeout     = Objects.requireNonNull(batchTimeout);
        this.recipientTimeout = Objects.requireNonNull(recipientTimeout);
        this.executor         = VirtualThreadExecutorFactory.create("arka-whatsapp-batch-");
    }

    @Override
    public CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> phoneNumbers, Map<String, Object> model) {
        Objects.requireNonNull(flowKey, "flowKey must not be null");
        Objects.requireNonNull(phoneNumbers, "phoneNumbers must not be null");
        Objects.requireNonNull(model, "model must not be null");

        if (phoneNumbers.isEmpty()) {
            return CompletableFuture.completedFuture(new BatchDeliveryResult(flowKey, List.of()));
        }

        var batchId = "batch-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        MDC.put("batchId", batchId);
        log.info("{} Starting batch [flowKey={}, recipients={}, batchId={}]",
                LOG_PREFIX, flowKey, phoneNumbers.size(), batchId);

        try {
            return fanOut(flowKey, phoneNumbers, model, batchId);
        } finally {
            MDC.remove("batchId");
        }
    }

    private CompletableFuture<BatchDeliveryResult> fanOut(
            String flowKey, List<String> phoneNumbers,
            Map<String, Object> model, String batchId) {

        var futures = new ArrayList<CompletableFuture<DeliveryResult>>(phoneNumbers.size());

        for (var phone : phoneNumbers) {
            try {
                throttle.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ArkaTaskInterruptedException(ArkaCoreCode.UNEXPECTED, "Batch dispatch interrupted", e);
            }

            Runnable task = () -> {
                var command = WhatsAppSendCommand.builder(flowKey).to(phone).model(model).build();
                sendHandler.handle(command);
            };

            var wrappedTask = propagatorChain.wrap(task);

            CompletableFuture<DeliveryResult> future;
            try {
                future = CompletableFuture
                        .runAsync(wrappedTask, executor)
                        .orTimeout(recipientTimeout.toMillis(), TimeUnit.MILLISECONDS)
                        .handle((v, ex) -> {
                            throttle.release();
                            if (ex == null) return DeliveryResult.success(flowKey);
                            log.warn("{} Recipient send failed [phone={}, batchId={}]: {}",
                                    LOG_PREFIX, mask(phone), batchId, ex.getMessage());
                            return DeliveryResult.failure(flowKey, ex);
                        });
            } catch (RejectedExecutionException ex) {
                throttle.release();
                throw new BatchDispatcherUnavailableException(ArkaCoreCode.UNEXPECTED, "Batch executor rejected task", ex);
            }

            futures.add(future);
        }

        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .orTimeout(batchTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .thenApply(v -> {
                    var results = futures.stream()
                            .map(f -> f.getNow(DeliveryResult.failure(flowKey, "unknown")))
                            .toList();
                    var result = new BatchDeliveryResult(flowKey, results);
                    log.info("{} Batch complete [flowKey={}, success={}, failed={}, batchId={}]",
                            LOG_PREFIX, flowKey, result.successCount(), result.failureCount(), batchId);
                    return result;
                });
    }

    private static String mask(String value) {
        if (value == null || value.length() <= 4) return "***";
        return value.substring(0, 3) + "***" + value.substring(value.length() - 2);
    }
}
