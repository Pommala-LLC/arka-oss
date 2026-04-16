package com.pommala.arka.async.autoconfigure;

import com.pommala.arka.async.internal.ExecutionContextPropagatorChain;
import com.pommala.arka.async.internal.MdcContextPropagator;
import com.pommala.arka.async.internal.SemaphoreThrottlePolicy;
import com.pommala.arka.spi.ExecutionContextPropagator;
import com.pommala.arka.spi.ThrottlePolicy;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for the shared async execution infrastructure. */
@AutoConfiguration
@EnableConfigurationProperties(ArkaAsyncProperties.class)
public class ArkaAsyncAutoConfiguration {

    /**
     * MDC context propagator — always registered as an additive SPI bean.
     * Pluggability rule: backs off when consumer provides own {@link MdcContextPropagator}.
     */
    @Bean
    @ConditionalOnMissingBean(MdcContextPropagator.class)
    public MdcContextPropagator mdcContextPropagator() {
        return new MdcContextPropagator();
    }

    /** Propagator chain — wraps all registered propagators. */
    @Bean
    @ConditionalOnMissingBean(ExecutionContextPropagatorChain.class)
    public ExecutionContextPropagatorChain executionContextPropagatorChain(
            List<ExecutionContextPropagator> propagators) {
        return new ExecutionContextPropagatorChain(propagators);
    }

    /**
     * Semaphore throttle policy.
     * Pluggability rule: backs off when consumer provides own {@link ThrottlePolicy}.
     */
    @Bean
    @ConditionalOnMissingBean(ThrottlePolicy.class)
    public SemaphoreThrottlePolicy semaphoreThrottlePolicy(ArkaAsyncProperties props) {
        return new SemaphoreThrottlePolicy(props.getMaxConcurrentSends());
    }
}
