package com.pommala.arka.slack.service.autoconfigure;

import com.pommala.arka.async.autoconfigure.ArkaAsyncProperties;
import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.slack.api.SlackBatchDispatcher;
import com.pommala.arka.slack.service.internal.*;
import com.pommala.arka.slack.spi.SlackFlowValidator;
import com.pommala.arka.slack.spi.SlackMessageBuilder;
import com.pommala.arka.slack.spi.SlackTransport;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SlackServiceAutoConfiguration {

    @Bean @ConditionalOnMissingBean(YamlSlackFlowProvider.class)
    public YamlSlackFlowProvider yamlSlackFlowProvider(YamlFlowProperties props) { return new YamlSlackFlowProvider(props); }

    @Bean @ConditionalOnMissingBean(SlackFlowValidator.class)
    public DefaultSlackFlowValidator defaultSlackFlowValidator(YamlSlackFlowProvider provider) { return new DefaultSlackFlowValidator(provider); }

    @Bean @ConditionalOnMissingBean(SlackMessageBuilder.class)
    public DefaultSlackMessageBuilder defaultSlackMessageBuilder() { return new DefaultSlackMessageBuilder(); }

    @Bean @ConditionalOnMissingBean(DefaultSlackSendHandler.class)
    public DefaultSlackSendHandler defaultSlackSendHandler(YamlSlackFlowProvider flowProvider, SlackMessageBuilder messageBuilder,
            SlackTransport transport, Optional<RetryPolicy> retryPolicy) {
        return new DefaultSlackSendHandler(flowProvider, messageBuilder, transport, retryPolicy);
    }

    @Bean @ConditionalOnMissingBean(SlackBatchDispatcher.class)
    public VirtualThreadSlackBatchDispatcher virtualThreadSlackBatchDispatcher(DefaultSlackSendHandler sendHandler,
            ExecutionContextPropagatorChain propagatorChain, ThrottlePolicy throttle, ArkaAsyncProperties asyncProps) {
        return new VirtualThreadSlackBatchDispatcher(sendHandler, propagatorChain, throttle, asyncProps.getBatchTimeout(), asyncProps.getRecipientTimeout());
    }
}
