package com.pommala.arka.teams.service.internal;

import com.pommala.arka.api.SendCommand;
import com.pommala.arka.model.DeliveryResult;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.teams.api.TeamsSendCommand;
import com.pommala.arka.teams.spi.TeamsMessageBuilder;
import com.pommala.arka.teams.spi.TeamsTransport;
import com.pommala.arka.teams.support.code.TeamsTransportCode;
import com.pommala.arka.teams.support.exception.TeamsTransportException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DefaultTeamsSendHandler implements ChannelSendHandler {
    public static final String CHANNEL_ID = "teams";
    private static final String LOG_PREFIX = "[arka-teams-service]";
    private static final String UNTRACED = "untraced";
    private static final Logger log = LoggerFactory.getLogger(DefaultTeamsSendHandler.class);

    private final YamlTeamsFlowProvider flowProvider;
    private final TeamsMessageBuilder messageBuilder;
    private final TeamsTransport transport;
    private final Optional<RetryPolicy> retryPolicy;

    public DefaultTeamsSendHandler(YamlTeamsFlowProvider flowProvider, TeamsMessageBuilder messageBuilder,
                                    TeamsTransport transport, Optional<RetryPolicy> retryPolicy) {
        this.flowProvider = flowProvider; this.messageBuilder = messageBuilder;
        this.transport = transport; this.retryPolicy = retryPolicy;
    }
    @Override public String channelId() { return CHANNEL_ID; }

    @Override
    public DeliveryResult handle(SendCommand command) {
        if (!(command instanceof TeamsSendCommand teamsCommand))
            throw new IllegalArgumentException("DefaultTeamsSendHandler received non-teams command: " + command.getClass().getSimpleName());
        var rawCid = MDC.get("correlationId");
        if (rawCid == null || rawCid.isBlank()) log.warn("{} Missing correlationId [flowKey={}]", LOG_PREFIX, teamsCommand.flowKey());
        final var cid = (rawCid != null && !rawCid.isBlank()) ? rawCid : UNTRACED;
        MDC.put("correlationId", cid);
        log.info("{} Handling Teams send [flowKey={}, correlationId={}]", LOG_PREFIX, teamsCommand.flowKey(), cid);
        try {
            var flow = flowProvider.resolve(teamsCommand.flowKey());
            var message = messageBuilder.build(flow, teamsCommand);
            sendWithOptionalRetry(message);
            log.info("{} Teams message sent [flowKey={}, correlationId={}]", LOG_PREFIX, teamsCommand.flowKey(), cid);
            return DeliveryResult.success(teamsCommand.flowKey());
        } finally { MDC.remove("correlationId"); }
    }

    private void sendWithOptionalRetry(com.pommala.arka.teams.model.FinalTeamsMessage message) {
        if (retryPolicy.isEmpty()) { transport.send(message); return; }
        var policy = retryPolicy.get(); ArkaApplicationException lastEx = null;
        for (int attempt = 1; attempt <= policy.maxAttempts(); attempt++) {
            try { transport.send(message); return; }
            catch (ArkaApplicationException ex) {
                lastEx = ex; if (!policy.isRetriable(ex)) throw ex;
                if (attempt < policy.maxAttempts()) {
                    try { Thread.sleep(policy.delay().toMillis()); }
                    catch (InterruptedException ie) { Thread.currentThread().interrupt();
                        throw new TeamsTransportException(TeamsTransportCode.GRAPH_SEND_FAILED, "Interrupted during retry", ie); }
                }
            }
        }
        throw lastEx;
    }
}
