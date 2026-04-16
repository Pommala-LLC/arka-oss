package com.pommala.arka.teams.service.internal;

import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.async.internal.VirtualThreadExecutorFactory;
import com.pommala.arka.model.BatchDeliveryResult;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.support.code.ArkaCoreCode;
import com.pommala.arka.support.exception.ArkaTaskInterruptedException;
import com.pommala.arka.support.exception.BatchDispatcherUnavailableException;
import com.pommala.arka.teams.api.TeamsBatchDispatcher;
import com.pommala.arka.teams.api.TeamsSendCommand;
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

public class VirtualThreadTeamsBatchDispatcher implements TeamsBatchDispatcher {
    private static final String LOG_PREFIX = "[arka-teams-batch]";
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadTeamsBatchDispatcher.class);
    private final DefaultTeamsSendHandler sendHandler;
    private final ExecutionContextPropagatorChain propagatorChain;
    private final ThrottlePolicy throttle;
    private final Duration batchTimeout, recipientTimeout;
    private final ExecutorService executor;

    public VirtualThreadTeamsBatchDispatcher(DefaultTeamsSendHandler sendHandler,
            ExecutionContextPropagatorChain propagatorChain, ThrottlePolicy throttle,
            Duration batchTimeout, Duration recipientTimeout) {
        this.sendHandler = Objects.requireNonNull(sendHandler);
        this.propagatorChain = Objects.requireNonNull(propagatorChain);
        this.throttle = Objects.requireNonNull(throttle);
        this.batchTimeout = Objects.requireNonNull(batchTimeout);
        this.recipientTimeout = Objects.requireNonNull(recipientTimeout);
        this.executor = VirtualThreadExecutorFactory.create("arka-teams-batch-");
    }

    @Override
    public CompletableFuture<BatchDeliveryResult> dispatch(String flowKey, List<String> channelIds, Map<String, Object> model) {
        Objects.requireNonNull(flowKey); Objects.requireNonNull(channelIds); Objects.requireNonNull(model);
        if (channelIds.isEmpty()) return CompletableFuture.completedFuture(new BatchDeliveryResult(flowKey, List.of()));
        var batchId = "batch-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        MDC.put("batchId", batchId);
        log.info("{} Starting batch [flowKey={}, targets={}, batchId={}]", LOG_PREFIX, flowKey, channelIds.size(), batchId);
        try { return fanOut(flowKey, channelIds, model, batchId); } finally { MDC.remove("batchId"); }
    }

    private CompletableFuture<BatchDeliveryResult> fanOut(String flowKey, List<String> ids, Map<String, Object> model, String batchId) {
        var futures = new ArrayList<CompletableFuture<DeliveryResult>>(ids.size());
        for (var chId : ids) {
            try { throttle.acquire(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); throw new ArkaTaskInterruptedException(ArkaCoreCode.UNEXPECTED, "Interrupted", e); }
            Runnable task = () -> sendHandler.handle(TeamsSendCommand.builder(flowKey).channelId(chId).model(model).build());
            var wrapped = propagatorChain.wrap(task);
            CompletableFuture<DeliveryResult> future;
            try {
                future = CompletableFuture.runAsync(wrapped, executor).orTimeout(recipientTimeout.toMillis(), TimeUnit.MILLISECONDS)
                    .handle((v, ex) -> { throttle.release(); return ex == null ? DeliveryResult.success(flowKey) : DeliveryResult.failure(flowKey, ex); });
            } catch (RejectedExecutionException ex) { throttle.release(); throw new BatchDispatcherUnavailableException(ArkaCoreCode.UNEXPECTED, "Rejected", ex); }
            futures.add(future);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).orTimeout(batchTimeout.toMillis(), TimeUnit.MILLISECONDS)
            .thenApply(v -> new BatchDeliveryResult(flowKey, futures.stream().map(f -> f.getNow(DeliveryResult.failure(flowKey, "unknown"))).toList()));
    }
}
