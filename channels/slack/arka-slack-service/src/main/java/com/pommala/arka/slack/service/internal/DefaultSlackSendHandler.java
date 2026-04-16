package com.pommala.arka.slack.service.internal;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.slack.api.SlackSendCommand;
import com.pommala.arka.slack.spi.SlackMessageBuilder;
import com.pommala.arka.slack.spi.SlackTransport;
import com.pommala.arka.slack.support.code.SlackTransportCode;
import com.pommala.arka.slack.support.exception.SlackTransportException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DefaultSlackSendHandler implements ChannelSendHandler {

    public static final String CHANNEL_ID = "slack";
    private static final String LOG_PREFIX = "[arka-slack-service]";
    private static final String UNTRACED = "untraced";
    private static final Logger log = LoggerFactory.getLogger(DefaultSlackSendHandler.class);

    private final YamlSlackFlowProvider flowProvider;
    private final SlackMessageBuilder messageBuilder;
    private final SlackTransport transport;
    private final Optional<RetryPolicy> retryPolicy;

    public DefaultSlackSendHandler(YamlSlackFlowProvider flowProvider, SlackMessageBuilder messageBuilder,
                                    SlackTransport transport, Optional<RetryPolicy> retryPolicy) {
        this.flowProvider = flowProvider; this.messageBuilder = messageBuilder;
        this.transport = transport; this.retryPolicy = retryPolicy;
    }

    @Override public String channelId() { return CHANNEL_ID; }

    @Override
    public DeliveryResult handle(SendCommand command) {
        if (!(command instanceof SlackSendCommand slackCommand))
            throw new IllegalArgumentException("DefaultSlackSendHandler received non-slack command: " + command.getClass().getSimpleName());

        var rawCid = MDC.get("correlationId");
        if (rawCid == null || rawCid.isBlank()) log.warn("{} Missing correlationId [flowKey={}]", LOG_PREFIX, slackCommand.flowKey());
        final var cid = (rawCid != null && !rawCid.isBlank()) ? rawCid : UNTRACED;
        MDC.put("correlationId", cid);
        log.info("{} Handling Slack send [flowKey={}, correlationId={}]", LOG_PREFIX, slackCommand.flowKey(), cid);
        try {
            var flow = flowProvider.resolve(slackCommand.flowKey());
            var message = messageBuilder.build(flow, slackCommand);
            sendWithOptionalRetry(message);
            log.info("{} Slack message sent [flowKey={}, correlationId={}]", LOG_PREFIX, slackCommand.flowKey(), cid);
            return DeliveryResult.success(slackCommand.flowKey());
        } finally { MDC.remove("correlationId"); }
    }

    private void sendWithOptionalRetry(com.pommala.arka.slack.model.FinalSlackMessage message) {
        if (retryPolicy.isEmpty()) { transport.send(message); return; }
        var policy = retryPolicy.get(); ArkaApplicationException lastEx = null;
        for (int attempt = 1; attempt <= policy.maxAttempts(); attempt++) {
            try { transport.send(message); return; }
            catch (ArkaApplicationException ex) {
                lastEx = ex;
                if (!policy.isRetriable(ex)) throw ex;
                if (attempt < policy.maxAttempts()) {
                    try { Thread.sleep(policy.delay().toMillis()); }
                    catch (InterruptedException ie) { Thread.currentThread().interrupt();
                        throw new SlackTransportException(SlackTransportCode.SEND_FAILED, "Interrupted during retry", ie); }
                }
            }
        }
        throw lastEx;
    }
}
