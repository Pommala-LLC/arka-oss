package com.pommala.arka.whatsapp.service.autoconfigure;

import com.pommala.arka.async.autoconfigure.ArkaAsyncProperties;
import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.spi.ThrottlePolicy;
import com.pommala.arka.whatsapp.api.WhatsAppBatchDispatcher;
import com.pommala.arka.whatsapp.service.internal.DefaultWhatsAppFlowValidator;
import com.pommala.arka.whatsapp.service.internal.DefaultWhatsAppMessageBuilder;
import com.pommala.arka.whatsapp.service.internal.DefaultWhatsAppSendHandler;
import com.pommala.arka.whatsapp.service.internal.VirtualThreadWhatsAppBatchDispatcher;
import com.pommala.arka.whatsapp.service.internal.YamlWhatsAppFlowProvider;
import com.pommala.arka.whatsapp.spi.WhatsAppFlowValidator;
import com.pommala.arka.whatsapp.spi.WhatsAppMessageBuilder;
import com.pommala.arka.whatsapp.spi.WhatsAppTransport;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class WhatsappServiceAutoConfiguration {

    @Bean @ConditionalOnMissingBean(YamlWhatsAppFlowProvider.class)
    public YamlWhatsAppFlowProvider yamlWhatsAppFlowProvider(YamlFlowProperties props) {
        return new YamlWhatsAppFlowProvider(props);
    }

    @Bean @ConditionalOnMissingBean(WhatsAppFlowValidator.class)
    public DefaultWhatsAppFlowValidator defaultWhatsAppFlowValidator(YamlWhatsAppFlowProvider provider) {
        return new DefaultWhatsAppFlowValidator(provider);
    }

    @Bean @ConditionalOnMissingBean(WhatsAppMessageBuilder.class)
    public DefaultWhatsAppMessageBuilder defaultWhatsAppMessageBuilder() {
        return new DefaultWhatsAppMessageBuilder();
    }

    @Bean @ConditionalOnMissingBean(DefaultWhatsAppSendHandler.class)
    public DefaultWhatsAppSendHandler defaultWhatsAppSendHandler(
            YamlWhatsAppFlowProvider flowProvider, WhatsAppMessageBuilder messageBuilder,
            WhatsAppTransport transport, Optional<RetryPolicy> retryPolicy) {
        return new DefaultWhatsAppSendHandler(flowProvider, messageBuilder, transport, retryPolicy);
    }

    @Bean @ConditionalOnMissingBean(WhatsAppBatchDispatcher.class)
    public VirtualThreadWhatsAppBatchDispatcher virtualThreadWhatsAppBatchDispatcher(
            DefaultWhatsAppSendHandler sendHandler, ExecutionContextPropagatorChain propagatorChain,
            ThrottlePolicy throttle, ArkaAsyncProperties asyncProps) {
        return new VirtualThreadWhatsAppBatchDispatcher(sendHandler, propagatorChain, throttle,
                asyncProps.getBatchTimeout(), asyncProps.getRecipientTimeout());
    }
}
