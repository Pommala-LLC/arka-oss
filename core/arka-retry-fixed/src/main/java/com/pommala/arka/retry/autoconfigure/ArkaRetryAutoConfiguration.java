package com.pommala.arka.retry.autoconfigure;

import com.pommala.arka.retry.internal.FixedRetryPolicy;
import com.pommala.arka.spi.RetryPolicy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for the fixed retry policy. */
@AutoConfiguration
@EnableConfigurationProperties(ArkaRetryProperties.class)
public class ArkaRetryAutoConfiguration {

    /**
     * Fixed-interval retry policy.
     * Pluggability rule: backs off when consumer provides own {@link RetryPolicy}.
     */
    @Bean
    @ConditionalOnMissingBean(RetryPolicy.class)
    public FixedRetryPolicy fixedRetryPolicy(ArkaRetryProperties props) {
        return new FixedRetryPolicy(props.getMaxAttempts(), props.getDelay());
    }
}
