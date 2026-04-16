package com.pommala.arka.service.internal;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.SendInterceptor;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.ArkaConfigurationException;
import com.pommala.arka.support.code.ArkaCoreCode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default shared send orchestration engine.
 *
 * <p>Routes {@link SendCommand} instances to the registered
 * {@link ChannelSendHandler} and calls all {@link SendInterceptor} beans.
 * Owns no channel-specific logic — channel handlers own their full pipelines.
 *
 * <h3>Send sequence:</h3>
 * <ol>
 *   <li>Locate handler by {@code command.channelId()}.</li>
 *   <li>Call {@code SendInterceptor.beforeSend()} on all interceptors.</li>
 *   <li>Delegate to {@code ChannelSendHandler.handle(command)}.</li>
 *   <li>Call {@code SendInterceptor.afterSuccess()} or {@code afterFailure()}.</li>
 * </ol>
 *
 * @see MessageFlowService
 * @see ChannelSendHandler
 */
public class DefaultMessageFlowService implements MessageFlowService {

    /** Domain log prefix for all log lines in this module. */
    private static final String LOG_PREFIX = "[arka-service]";

    private static final Logger log = LoggerFactory.getLogger(DefaultMessageFlowService.class);

    private final Map<String, ChannelSendHandler> handlersByChannel;
    private final List<SendInterceptor> interceptors;

    public DefaultMessageFlowService(List<ChannelSendHandler> handlers,
                                     List<SendInterceptor> interceptors) {
        Objects.requireNonNull(handlers, "handlers must not be null");
        Objects.requireNonNull(interceptors, "interceptors must not be null");
        this.handlersByChannel = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ChannelSendHandler::channelId,
                        Function.identity()));
        this.interceptors = List.copyOf(interceptors);
    }

    @Override
    public DeliveryResult send(SendCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        var handler = resolveHandler(command);
        var start = Instant.now();

        log.info("{} Starting send [flowKey={}]", LOG_PREFIX, command.flowKey());

        // Business rule: interceptors are additive — all beans called in registration order.
        // Interceptors must never throw — failures are swallowed to protect the send pipeline.
        callBeforeSend(command);

        try {
            var result = handler.handle(command);
            var elapsed = Duration.between(start, Instant.now());
            log.info("{} Send completed [flowKey={}, elapsed={}ms]",
                    LOG_PREFIX, command.flowKey(), elapsed.toMillis());
            callAfterSuccess(command, elapsed, result);
            return result;

        } catch (ArkaApplicationException ex) {
            var elapsed = Duration.between(start, Instant.now());
            log.error("{} Send failed [flowKey={}, code={}, elapsed={}ms]",
                    LOG_PREFIX, command.flowKey(), ex.internalCode().code(), elapsed.toMillis(), ex);
            callAfterFailure(command, elapsed, ex);
            throw ex;
        }
    }

    private ChannelSendHandler resolveHandler(SendCommand command) {
        // Business rule: channel must have exactly one registered handler.
        // Missing handler = misconfiguration — fail fast.
        var channelId = resolveChannelId(command);
        var handler = handlersByChannel.get(channelId);
        if (handler == null) {
            throw new ArkaConfigurationException(ArkaCoreCode.UNEXPECTED,
                    "No ChannelSendHandler registered for channel: " + channelId);
        }
        return handler;
    }

    private String resolveChannelId(SendCommand command) {
        // Each channel command type maps to a known channel ID.
        // Reflection-free: channel handlers registered by channel ID string.
        var className = command.getClass().getSimpleName();
        return className.replace("SendCommand", "").toLowerCase();
    }

    private void callBeforeSend(SendCommand command) {
        for (var interceptor : interceptors) {
            try {
                interceptor.beforeSend(command);
            } catch (Exception ex) {
                // Interceptors must never abort sends — swallow and warn.
                log.warn("{} SendInterceptor.beforeSend() threw — swallowed [interceptor={}, error={}]",
                        LOG_PREFIX, interceptor.getClass().getSimpleName(), ex.getMessage());
            }
        }
    }

    private void callAfterSuccess(SendCommand command, Duration elapsed, DeliveryResult result) {
        for (var interceptor : interceptors) {
            try {
                interceptor.afterSuccess(command, elapsed, result);
            } catch (Exception ex) {
                log.warn("{} SendInterceptor.afterSuccess() threw — swallowed [interceptor={}]",
                        LOG_PREFIX, interceptor.getClass().getSimpleName());
            }
        }
    }

    private void callAfterFailure(SendCommand command, Duration elapsed, ArkaApplicationException ex) {
        for (var interceptor : interceptors) {
            try {
                interceptor.afterFailure(command, elapsed, ex);
            } catch (Exception swallowed) {
                log.warn("{} SendInterceptor.afterFailure() threw — swallowed [interceptor={}]",
                        LOG_PREFIX, interceptor.getClass().getSimpleName());
            }
        }
    }
}
