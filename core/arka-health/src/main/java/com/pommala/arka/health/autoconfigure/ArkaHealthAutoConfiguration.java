package com.pommala.arka.health.autoconfigure;

import com.pommala.arka.health.internal.ChannelRegistrationHealthIndicator;
import com.pommala.arka.health.internal.FlowConfigurationHealthIndicator;
import com.pommala.arka.spi.ChannelFlowValidator;
import com.pommala.arka.spi.ChannelSendHandler;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for Arka health indicators.
 *
 * <p>Registers plain health-check beans. When Spring Boot Actuator is
 * on the classpath, the {@link ArkaActuatorHealthAutoConfiguration}
 * inner config adapts them to {@code HealthIndicator} beans.
 */
@AutoConfiguration
public class ArkaHealthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ChannelRegistrationHealthIndicator.class)
    public ChannelRegistrationHealthIndicator channelRegistrationHealthIndicator(
            List<ChannelSendHandler> handlers) {
        return new ChannelRegistrationHealthIndicator(handlers);
    }

    @Bean
    @ConditionalOnMissingBean(FlowConfigurationHealthIndicator.class)
    public FlowConfigurationHealthIndicator flowConfigurationHealthIndicator(
            List<ChannelFlowValidator> validators) {
        return new FlowConfigurationHealthIndicator(validators);
    }
}
