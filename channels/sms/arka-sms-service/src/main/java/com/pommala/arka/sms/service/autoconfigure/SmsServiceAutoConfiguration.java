package com.pommala.arka.sms.service.autoconfigure;

import com.pommala.arka.async.autoconfigure.ArkaAsyncProperties;
import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.sms.api.SmsBatchDispatcher;
import com.pommala.arka.sms.service.internal.*;
import com.pommala.arka.sms.spi.SmsFlowValidator;
import com.pommala.arka.sms.spi.SmsMessageBuilder;
import com.pommala.arka.sms.spi.SmsTransport;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SmsServiceAutoConfiguration {

    @Bean @ConditionalOnMissingBean(YamlSmsFlowProvider.class)
    public YamlSmsFlowProvider yamlSmsFlowProvider(YamlFlowProperties props) { return new YamlSmsFlowProvider(props); }

    @Bean @ConditionalOnMissingBean(SmsFlowValidator.class)
    public DefaultSmsFlowValidator defaultSmsFlowValidator(YamlSmsFlowProvider provider) { return new DefaultSmsFlowValidator(provider); }

    @Bean @ConditionalOnMissingBean(SmsMessageBuilder.class)
    public DefaultSmsMessageBuilder defaultSmsMessageBuilder() { return new DefaultSmsMessageBuilder(); }

    @Bean @ConditionalOnMissingBean(DefaultSmsSendHandler.class)
    public DefaultSmsSendHandler defaultSmsSendHandler(YamlSmsFlowProvider flowProvider, SmsMessageBuilder messageBuilder,
            SmsTransport transport, Optional<RetryPolicy> retryPolicy) {
        return new DefaultSmsSendHandler(flowProvider, messageBuilder, transport, retryPolicy);
    }

    @Bean @ConditionalOnMissingBean(SmsBatchDispatcher.class)
    public VirtualThreadSmsBatchDispatcher virtualThreadSmsBatchDispatcher(DefaultSmsSendHandler sendHandler,
            ExecutionContextPropagatorChain propagatorChain, ThrottlePolicy throttle, ArkaAsyncProperties asyncProps) {
        return new VirtualThreadSmsBatchDispatcher(sendHandler, propagatorChain, throttle, asyncProps.getBatchTimeout(), asyncProps.getRecipientTimeout());
    }
}
