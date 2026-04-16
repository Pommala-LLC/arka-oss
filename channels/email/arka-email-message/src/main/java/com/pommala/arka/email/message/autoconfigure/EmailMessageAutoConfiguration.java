package com.pommala.arka.email.message.autoconfigure;

import com.pommala.arka.email.message.internal.DefaultEmailMessageBuilder;
import com.pommala.arka.email.spi.EmailMessageBuilder;
import com.pommala.arka.spi.TemplateRenderer;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for email message construction. */
@AutoConfiguration
public class EmailMessageAutoConfiguration {

    /**
     * Default email message builder.
     * Pluggability rule: backs off when consumer provides own {@link EmailMessageBuilder}.
     */
    @Bean
    @ConditionalOnMissingBean(EmailMessageBuilder.class)
    public DefaultEmailMessageBuilder defaultEmailMessageBuilder(
            Optional<TemplateRenderer> renderer) {
        return new DefaultEmailMessageBuilder(renderer);
    }
}
