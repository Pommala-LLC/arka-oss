package com.pommala.arka.email.service.internal;

import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.async.internal.VirtualThreadExecutorFactory;
import com.pommala.arka.email.api.EmailBatchDispatcher;
import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.model.BatchDeliveryResult;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.support.exception.ArkaTaskInterruptedException;
import com.pommala.arka.support.exception.BatchDispatcherUnavailableException;
import com.pommala.arka.support.code.ArkaCoreCode;
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
 * Virtual-thread based email batch dispatcher.
 *
 * <p>Fans out one {@link EmailSendCommand} per recipient using Java 21 virtual
 * threads. Each recipient receives a separate email — no recipient can see
 * other addresses.
 *
 * <p>Privacy rule (locked): batch dispatch never puts multiple recipients in a
 * single {@code to} field. One send per recipient, always.
 *
 * <p>Typed to {@link com.pommala.arka.email.model.FinalEmailMessage} — lives in
 * {@code arka-email-service}, not in shared {@code arka-async}.
 */
public class VirtualThreadEmailBatchDispatcher implements EmailBatchDispatcher {

    private static final String LOG_PREFIX = "[arka-email-batch]";
    private static final String UNTRACED   = "untraced";
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadEmailBatchDispatcher.class);

    private final DefaultEmailSendHandler sendHandler;
    private final ExecutionContextPropagatorChain propagatorChain;
    private final ThrottlePolicy throttle;
    private final Duration batchTimeout;
    private final Duration recipientTimeout;
    private final ExecutorService executor;

    public VirtualThreadEmailBatchDispatcher(DefaultEmailSendHandler sendHandler,
                                             ExecutionContextPropagatorChain propagatorChain,
                                             ThrottlePolicy throttle,
                                             Duration batchTimeout,
                                             Duration recipientTimeout) {
        this.sendHandler       = Objects.requireNonNull(sendHandler);
        this.propagatorChain   = Objects.requireNonNull(propagatorChain);
        this.throttle          = Objects.requireNonNull(throttle);
        this.batchTimeout      = Objects.requireNonNull(batchTimeout);
        this.recipientTimeout  = Objects.requireNonNull(recipientTimeout);
        this.executor          = VirtualThreadExecutorFactory.create("arka-email-batch-");
    }

    @Override
    public CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, List<String> addresses, Map<String, Object> model) {
        Objects.requireNonNull(flowKey,   "flowKey must not be null");
        Objects.requireNonNull(addresses, "addresses must not be null");
        Objects.requireNonNull(model,     "model must not be null");

        if (addresses.isEmpty()) {
            return CompletableFuture.completedFuture(
                    new BatchDeliveryResult(flowKey, List.of()));
        }

        var batchId = generateBatchId();
        MDC.put("batchId", batchId);
        log.info("{} Starting batch [flowKey={}, recipients={}, batchId={}]",
                LOG_PREFIX, flowKey, addresses.size(), batchId);

        try {
            return fanOut(flowKey, addresses, model, batchId);
        } finally {
            MDC.remove("batchId");
        }
    }

    @Override
    public CompletableFuture<BatchDeliveryResult> dispatch(
            String flowKey, Map<String, Map<String, Object>> modelByAddress) {
        Objects.requireNonNull(flowKey,        "flowKey must not be null");
        Objects.requireNonNull(modelByAddress, "modelByAddress must not be null");

        return dispatch(flowKey, new ArrayList<>(modelByAddress.keySet()),
                modelByAddress.isEmpty() ? Map.of() : modelByAddress.values().iterator().next());
    }

    private CompletableFuture<BatchDeliveryResult> fanOut(
            String flowKey, List<String> addresses,
            Map<String, Object> model, String batchId) {

        var futures = new ArrayList<CompletableFuture<DeliveryResult>>(addresses.size());

        for (var address : addresses) {
            CompletableFuture<DeliveryResult> future;
            try {
                // Throttling rule: semaphore acquired before task submission.
                throttle.acquire();
            } catch (InterruptedException e) {
                // InterruptedException rule: always restore interrupt flag.
                Thread.currentThread().interrupt();
                throw new ArkaTaskInterruptedException(
                        ArkaCoreCode.UNEXPECTED, "Batch dispatch interrupted", e);
            }

            Runnable task = () -> {
                var command = EmailSendCommand.builder(flowKey)
                        .to(address)
                        .model(model)
                        .build();
                sendHandler.handle(command);
            };

            // Wrap through all propagators — preserves MDC, tenant, trace contexts.
            var wrappedTask = propagatorChain.wrap(task);

            try {
                future = CompletableFuture
                        .runAsync(wrappedTask, executor)
                        .orTimeout(recipientTimeout.toMillis(), TimeUnit.MILLISECONDS)
                        .handle((v, ex) -> {
                            throttle.release();
                            if (ex == null) return DeliveryResult.success(flowKey);
                            log.warn("{} Recipient send failed [address={}, batchId={}]: {}",
                                    LOG_PREFIX, mask(address), batchId, ex.getMessage());
                            return DeliveryResult.failure(flowKey, ex);
                        });
            } catch (RejectedExecutionException ex) {
                throttle.release();
                // Async rule: rejected execution translated to typed exception.
                throw new BatchDispatcherUnavailableException(
                        ArkaCoreCode.UNEXPECTED, "Batch executor rejected task", ex);
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
                            LOG_PREFIX, flowKey, result.successCount(),
                            result.failureCount(), batchId);
                    return result;
                });
    }

    private String generateBatchId() {
        // Tracking ID rule: batchId is the one exception — created by the batch
        // dispatcher at batch creation, propagated downstream via MDC.
        return "batch-" + java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    private static String mask(String value) {
        if (value == null || value.length() <= 2) return "***";
        return value.charAt(0) + "***" + value.substring(value.length() - 1);
    }
}
