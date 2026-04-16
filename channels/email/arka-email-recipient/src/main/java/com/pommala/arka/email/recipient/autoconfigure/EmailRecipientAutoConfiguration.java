package com.pommala.arka.email.recipient.autoconfigure;

import com.pommala.arka.email.recipient.internal.DefaultRecipientNormalizer;
import com.pommala.arka.email.recipient.internal.RecipientPolicy;
import com.pommala.arka.email.spi.RecipientPolicyHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for recipient normalization. */
@AutoConfiguration
@EnableConfigurationProperties(RecipientPolicy.class)
public class EmailRecipientAutoConfiguration {

    /**
     * Default recipient normalizer.
     * Pluggability rule: backs off when consumer provides own {@link RecipientPolicyHandler}.
     */
    @Bean
    @ConditionalOnMissingBean(RecipientPolicyHandler.class)
    public DefaultRecipientNormalizer defaultRecipientNormalizer(RecipientPolicy policy) {
        return new DefaultRecipientNormalizer(policy);
    }
}
