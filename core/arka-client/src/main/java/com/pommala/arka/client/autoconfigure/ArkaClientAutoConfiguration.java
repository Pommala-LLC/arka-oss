package com.pommala.arka.client.autoconfigure;

import com.pommala.arka.client.ArkaClient;
import com.pommala.arka.client.internal.DefaultArkaClient;
import com.pommala.arka.client.properties.ArkaClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for the Arka typed client.
 *
 * <p>Activates only when {@code arka.client.base-url} is set —
 * the client is for calling Arka from another service, not for
 * Arka's own internal use.
 */
@AutoConfiguration
@EnableConfigurationProperties(ArkaClientProperties.class)
public class ArkaClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ArkaClient.class)
    @ConditionalOnProperty(prefix = "arka.client", name = "base-url")
    public DefaultArkaClient arkaClient(ArkaClientProperties props) {
        return new DefaultArkaClient(props);
    }
}
