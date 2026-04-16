package com.pommala.arka.teams.service.autoconfigure;

import com.pommala.arka.async.autoconfigure.ArkaAsyncProperties;
import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.teams.api.TeamsBatchDispatcher;
import com.pommala.arka.teams.service.internal.*;
import com.pommala.arka.teams.spi.TeamsFlowValidator;
import com.pommala.arka.teams.spi.TeamsMessageBuilder;
import com.pommala.arka.teams.spi.TeamsTransport;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TeamsServiceAutoConfiguration {

    @Bean @ConditionalOnMissingBean(YamlTeamsFlowProvider.class)
    public YamlTeamsFlowProvider yamlTeamsFlowProvider(YamlFlowProperties props) { return new YamlTeamsFlowProvider(props); }

    @Bean @ConditionalOnMissingBean(TeamsFlowValidator.class)
    public DefaultTeamsFlowValidator defaultTeamsFlowValidator(YamlTeamsFlowProvider provider) { return new DefaultTeamsFlowValidator(provider); }

    @Bean @ConditionalOnMissingBean(TeamsMessageBuilder.class)
    public DefaultTeamsMessageBuilder defaultTeamsMessageBuilder() { return new DefaultTeamsMessageBuilder(); }

    @Bean @ConditionalOnMissingBean(DefaultTeamsSendHandler.class)
    public DefaultTeamsSendHandler defaultTeamsSendHandler(YamlTeamsFlowProvider flowProvider, TeamsMessageBuilder messageBuilder,
            TeamsTransport transport, Optional<RetryPolicy> retryPolicy) {
        return new DefaultTeamsSendHandler(flowProvider, messageBuilder, transport, retryPolicy);
    }

    @Bean @ConditionalOnMissingBean(TeamsBatchDispatcher.class)
    public VirtualThreadTeamsBatchDispatcher virtualThreadTeamsBatchDispatcher(DefaultTeamsSendHandler sendHandler,
            ExecutionContextPropagatorChain propagatorChain, ThrottlePolicy throttle, ArkaAsyncProperties asyncProps) {
        return new VirtualThreadTeamsBatchDispatcher(sendHandler, propagatorChain, throttle, asyncProps.getBatchTimeout(), asyncProps.getRecipientTimeout());
    }
}
