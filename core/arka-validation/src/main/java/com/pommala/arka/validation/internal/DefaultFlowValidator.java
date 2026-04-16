package com.pommala.arka.validation.internal;

import com.pommala.arka.spi.ChannelFlowValidator;
import com.pommala.arka.support.code.ArkaValidationCode;
import com.pommala.arka.support.exception.ArkaConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Shared startup validation engine.
 *
 * <p>Collects errors from all registered {@link ChannelFlowValidator} beans
 * and throws a single {@link ArkaConfigurationException} listing all violations.
 *
 * <p>Business rule: startup validation is fail-fast with full collection.
 * All errors collected before throwing — operator sees the complete picture.
 */
public class DefaultFlowValidator implements InitializingBean {

    private static final String LOG_PREFIX = "[arka-validation]";
    private static final Logger log = LoggerFactory.getLogger(DefaultFlowValidator.class);

    private final List<ChannelFlowValidator> channelValidators;

    public DefaultFlowValidator(List<ChannelFlowValidator> channelValidators) {
        Objects.requireNonNull(channelValidators, "channelValidators must not be null");
        this.channelValidators = List.copyOf(channelValidators);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("{} Running startup flow validation across {} channel(s)",
                LOG_PREFIX, channelValidators.size());

        // Business rule: startup validation is fail-fast with full collection.
        // All errors collected before throwing — operator sees the complete picture.
        var allErrors = new ArrayList<String>();

        for (var validator : channelValidators) {
            var errors = validator.validate();
            if (!errors.isEmpty()) {
                log.error("{} Validation errors in channel '{}': {}",
                        LOG_PREFIX, validator.channelId(), errors.size());
                allErrors.addAll(errors);
            }
        }

        if (!allErrors.isEmpty()) {
            var message = "Startup validation failed with " + allErrors.size() +
                    " error(s):\n" + String.join("\n", allErrors);
            throw new ArkaConfigurationException(ArkaValidationCode.VALIDATION_FAILED, message);
        }

        log.info("{} Startup validation passed", LOG_PREFIX);
    }
}
