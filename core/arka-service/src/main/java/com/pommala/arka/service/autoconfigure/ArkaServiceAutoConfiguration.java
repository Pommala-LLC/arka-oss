package com.pommala.arka.service.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.service.internal.DefaultMessageFlowService;
import com.pommala.arka.spi.ChannelSendHandler;
import com.pommala.arka.spi.SendInterceptor;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for the shared Arka send orchestration engine.
 *
 * <p>Registers {@link DefaultMessageFlowService} as the primary
 * {@link MessageFlowService} bean. Backs off via
 * {@code @ConditionalOnMissingBean} when the consumer provides their own.
 */
@AutoConfiguration
public class ArkaServiceAutoConfiguration {

    /**
     * Default send orchestration service.
     *
     * <p>Pluggability rule: backs off when consumer provides own {@link MessageFlowService}.
     */
    @Bean
    @ConditionalOnMissingBean(MessageFlowService.class)
    public DefaultMessageFlowService messageFlowService(
            List<ChannelSendHandler> handlers,
            List<SendInterceptor> interceptors) {
        return new DefaultMessageFlowService(handlers, interceptors);
    }
}
