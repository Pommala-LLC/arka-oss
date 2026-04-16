package com.pommala.arka.validation.autoconfigure;

import com.pommala.arka.spi.ChannelFlowValidator;
import com.pommala.arka.validation.internal.DefaultFlowValidator;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for the shared startup validation engine. */
@AutoConfiguration
public class ArkaValidationAutoConfiguration {

    @Bean
    public DefaultFlowValidator defaultFlowValidator(List<ChannelFlowValidator> validators) {
        return new DefaultFlowValidator(validators);
    }
}
