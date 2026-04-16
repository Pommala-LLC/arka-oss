package com.pommala.arka.whatsapp.service.internal;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import com.pommala.arka.whatsapp.spi.WhatsAppMessageBuilder;
import com.pommala.arka.whatsapp.spi.WhatsAppTransport;
import com.pommala.arka.whatsapp.support.code.WhatsAppTransportCode;
import com.pommala.arka.whatsapp.support.exception.WhatsAppTransportException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Default WhatsApp channel send pipeline.
 *
 * <p>Implements the full WhatsApp send sequence as a {@link ChannelSendHandler}:
 * <ol>
 *   <li>Flow resolution via {@link YamlWhatsAppFlowProvider}.</li>
 *   <li>Message construction via {@link WhatsAppMessageBuilder}.</li>
 *   <li>Transport send via {@link WhatsAppTransport} (with retry if policy present).</li>
 * </ol>
 */
public class DefaultWhatsAppSendHandler implements ChannelSendHandler {

    public static final String CHANNEL_ID = "whatsapp";

    private static final String LOG_PREFIX = "[arka-whatsapp-service]";
    private static final String UNTRACED = "untraced";
    private static final Logger log = LoggerFactory.getLogger(DefaultWhatsAppSendHandler.class);

    private final YamlWhatsAppFlowProvider flowProvider;
    private final WhatsAppMessageBuilder messageBuilder;
    private final WhatsAppTransport transport;
    private final Optional<RetryPolicy> retryPolicy;

    public DefaultWhatsAppSendHandler(YamlWhatsAppFlowProvider flowProvider,
                                       WhatsAppMessageBuilder messageBuilder,
                                       WhatsAppTransport transport,
                                       Optional<RetryPolicy> retryPolicy) {
        this.flowProvider   = flowProvider;
        this.messageBuilder = messageBuilder;
        this.transport      = transport;
        this.retryPolicy    = retryPolicy;
    }

    @Override
    public String channelId() { return CHANNEL_ID; }

    @Override
    public DeliveryResult handle(SendCommand command) {
        if (!(command instanceof WhatsAppSendCommand waCommand)) {
            throw new IllegalArgumentException(
                    "DefaultWhatsAppSendHandler received non-whatsapp command: " +
                    command.getClass().getSimpleName());
        }

        var rawCorrelationId = MDC.get("correlationId");
        if (rawCorrelationId == null || rawCorrelationId.isBlank()) {
            log.warn("{} Missing correlationId in MDC — using untraced [flowKey={}]",
                    LOG_PREFIX, waCommand.flowKey());
        }
        final var correlationId = (rawCorrelationId != null && !rawCorrelationId.isBlank())
                ? rawCorrelationId : UNTRACED;

        MDC.put("correlationId", correlationId);
        log.info("{} Handling WhatsApp send [flowKey={}, to={}, correlationId={}]",
                LOG_PREFIX, waCommand.flowKey(), mask(waCommand.to()), correlationId);

        try {
            return executeWhatsAppPipeline(waCommand, correlationId);
        } finally {
            MDC.remove("correlationId");
        }
    }

    private DeliveryResult executeWhatsAppPipeline(WhatsAppSendCommand command, String correlationId) {
        var flow = flowProvider.resolve(command.flowKey());
        var message = messageBuilder.build(flow, command);
        sendWithOptionalRetry(message);

        log.info("{} WhatsApp message sent [flowKey={}, correlationId={}]",
                LOG_PREFIX, command.flowKey(), correlationId);
        return DeliveryResult.success(command.flowKey());
    }

    private void sendWithOptionalRetry(com.pommala.arka.whatsapp.model.FinalWhatsAppMessage message) {
        if (retryPolicy.isEmpty()) {
            transport.send(message);
            return;
        }

        var policy = retryPolicy.get();
        var maxAttempts = policy.maxAttempts();
        ArkaApplicationException lastEx = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                transport.send(message);
                return;
            } catch (ArkaApplicationException ex) {
                lastEx = ex;
                if (!policy.isRetriable(ex)) {
                    log.warn("{} Non-retriable transport failure [attempt={}, code={}]",
                            LOG_PREFIX, attempt, ex.internalCode().code());
                    throw ex;
                }
                if (attempt < maxAttempts) {
                    log.warn("{} Retriable transport failure [attempt={}/{}, code={}] — retrying after {}ms",
                            LOG_PREFIX, attempt, maxAttempts, ex.internalCode().code(),
                            policy.delay().toMillis());
                    try {
                        Thread.sleep(policy.delay().toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new WhatsAppTransportException(
                                WhatsAppTransportCode.API_SEND_FAILED,
                                "Transport interrupted during retry delay", ie);
                    }
                }
            }
        }
        throw lastEx;
    }

    private static String mask(String value) {
        if (value == null || value.length() <= 4) return "***";
        return value.substring(0, 3) + "***" + value.substring(value.length() - 2);
    }
}
