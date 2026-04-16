package com.pommala.arka.sms.service.internal;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.sms.api.SmsSendCommand;
import com.pommala.arka.sms.spi.SmsMessageBuilder;
import com.pommala.arka.sms.spi.SmsTransport;
import com.pommala.arka.sms.support.code.SmsTransportCode;
import com.pommala.arka.sms.support.exception.SmsTransportException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DefaultSmsSendHandler implements ChannelSendHandler {

    public static final String CHANNEL_ID = "sms";
    private static final String LOG_PREFIX = "[arka-sms-service]";
    private static final String UNTRACED = "untraced";
    private static final Logger log = LoggerFactory.getLogger(DefaultSmsSendHandler.class);

    private final YamlSmsFlowProvider flowProvider;
    private final SmsMessageBuilder messageBuilder;
    private final SmsTransport transport;
    private final Optional<RetryPolicy> retryPolicy;

    public DefaultSmsSendHandler(YamlSmsFlowProvider flowProvider,
                                  SmsMessageBuilder messageBuilder,
                                  SmsTransport transport,
                                  Optional<RetryPolicy> retryPolicy) {
        this.flowProvider   = flowProvider;
        this.messageBuilder = messageBuilder;
        this.transport      = transport;
        this.retryPolicy    = retryPolicy;
    }

    @Override public String channelId() { return CHANNEL_ID; }

    @Override
    public DeliveryResult handle(SendCommand command) {
        if (!(command instanceof SmsSendCommand smsCommand)) {
            throw new IllegalArgumentException(
                    "DefaultSmsSendHandler received non-sms command: " + command.getClass().getSimpleName());
        }
        var rawCorrelationId = MDC.get("correlationId");
        if (rawCorrelationId == null || rawCorrelationId.isBlank()) {
            log.warn("{} Missing correlationId in MDC — using untraced [flowKey={}]",
                    LOG_PREFIX, smsCommand.flowKey());
        }
        final var correlationId = (rawCorrelationId != null && !rawCorrelationId.isBlank())
                ? rawCorrelationId : UNTRACED;
        MDC.put("correlationId", correlationId);
        log.info("{} Handling SMS send [flowKey={}, correlationId={}]",
                LOG_PREFIX, smsCommand.flowKey(), correlationId);
        try {
            var flow = flowProvider.resolve(smsCommand.flowKey());
            var message = messageBuilder.build(flow, smsCommand);
            sendWithOptionalRetry(message);
            log.info("{} SMS sent [flowKey={}, correlationId={}]",
                    LOG_PREFIX, smsCommand.flowKey(), correlationId);
            return DeliveryResult.success(smsCommand.flowKey());
        } finally {
            MDC.remove("correlationId");
        }
    }

    private void sendWithOptionalRetry(com.pommala.arka.sms.model.FinalSmsMessage message) {
        if (retryPolicy.isEmpty()) { transport.send(message); return; }
        var policy = retryPolicy.get();
        ArkaApplicationException lastEx = null;
        for (int attempt = 1; attempt <= policy.maxAttempts(); attempt++) {
            try { transport.send(message); return; }
            catch (ArkaApplicationException ex) {
                lastEx = ex;
                if (!policy.isRetriable(ex)) { throw ex; }
                if (attempt < policy.maxAttempts()) {
                    try { Thread.sleep(policy.delay().toMillis()); }
                    catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SmsTransportException(SmsTransportCode.SEND_FAILED,
                                "Transport interrupted during retry delay", ie);
                    }
                }
            }
        }
        throw lastEx;
    }
}
