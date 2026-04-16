package com.pommala.arka.security.autoconfigure;

import com.pommala.arka.security.filter.ArkaApiKeyAuthFilter;
import com.pommala.arka.security.properties.ArkaSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/** Auto-configuration for Arka API key security. */
@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties(ArkaSecurityProperties.class)
public class ArkaSecurityAutoConfiguration {

    /**
     * API key auth filter — registered only when enabled via properties.
     *
     * <p>Pluggability rule: backs off when consumer provides own
     * {@link ArkaApiKeyAuthFilter}.
     */
    @Bean
    @ConditionalOnProperty(prefix = "arka.security.api-key", name = "enabled", havingValue = "true")
    public FilterRegistrationBean<ArkaApiKeyAuthFilter> arkaApiKeyAuthFilter(
            ArkaSecurityProperties props) {
        var registration = new FilterRegistrationBean<>(new ArkaApiKeyAuthFilter(props));
        registration.addUrlPatterns("/api/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
