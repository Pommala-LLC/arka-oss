package com.pommala.arka.email.service.autoconfigure;

import com.pommala.arka.async.autoconfigure.ArkaAsyncProperties;
import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.email.api.EmailBatchDispatcher;
import com.pommala.arka.email.spi.EmailFlowValidator;
import com.pommala.arka.email.spi.EmailMessageBuilder;
import com.pommala.arka.email.spi.EmailTransport;
import com.pommala.arka.email.spi.RecipientPolicyHandler;
import com.pommala.arka.email.service.internal.DefaultEmailFlowValidator;
import com.pommala.arka.email.service.internal.DefaultEmailSendHandler;
import com.pommala.arka.email.service.internal.VirtualThreadEmailBatchDispatcher;
import com.pommala.arka.email.service.internal.YamlEmailFlowProvider;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.spi.ThrottlePolicy;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for the email channel send pipeline.
 *
 * <p>Registers the YAML flow provider, default send handler, flow validator,
 * and virtual thread batch dispatcher.
 */
@AutoConfiguration
public class EmailServiceAutoConfiguration {

    /** YAML email flow provider. */
    @Bean
    @ConditionalOnMissingBean(YamlEmailFlowProvider.class)
    public YamlEmailFlowProvider yamlEmailFlowProvider(YamlFlowProperties props) {
        return new YamlEmailFlowProvider(props);
    }

    /** Default email flow validator. Contributes to shared startup validation engine. */
    @Bean
    @ConditionalOnMissingBean(EmailFlowValidator.class)
    public DefaultEmailFlowValidator defaultEmailFlowValidator(YamlEmailFlowProvider provider) {
        return new DefaultEmailFlowValidator(provider);
    }

    /**
     * Default email send handler — the channel's {@link ChannelSendHandler}.
     *
     * <p>Pluggability rule: backs off when consumer provides own
     * {@link DefaultEmailSendHandler}.
     */
    @Bean
    @ConditionalOnMissingBean(DefaultEmailSendHandler.class)
    public DefaultEmailSendHandler defaultEmailSendHandler(
            YamlEmailFlowProvider flowProvider,
            RecipientPolicyHandler recipientHandler,
            EmailMessageBuilder messageBuilder,
            EmailTransport transport,
            Optional<RetryPolicy> retryPolicy) {
        return new DefaultEmailSendHandler(
                flowProvider, recipientHandler, messageBuilder, transport, retryPolicy);
    }

    /**
     * Virtual thread email batch dispatcher.
     *
     * <p>Pluggability rule: backs off when consumer provides own
     * {@link EmailBatchDispatcher}.
     */
    @Bean
    @ConditionalOnMissingBean(EmailBatchDispatcher.class)
    public VirtualThreadEmailBatchDispatcher virtualThreadEmailBatchDispatcher(
            DefaultEmailSendHandler sendHandler,
            ExecutionContextPropagatorChain propagatorChain,
            ThrottlePolicy throttle,
            ArkaAsyncProperties asyncProps) {
        return new VirtualThreadEmailBatchDispatcher(
                sendHandler, propagatorChain, throttle,
                asyncProps.getBatchTimeout(), asyncProps.getRecipientTimeout());
    }
}
