package com.pommala.arka.email.template.thymeleaf.autoconfigure;

import com.pommala.arka.email.template.thymeleaf.internal.ThymeleafTemplateProbe;
import com.pommala.arka.email.template.thymeleaf.internal.ThymeleafTemplateRenderer;
import com.pommala.arka.spi.TemplateProbe;
import com.pommala.arka.spi.TemplateRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.ITemplateEngine;

/** Auto-configuration for Thymeleaf template rendering. */
@AutoConfiguration
@ConditionalOnClass(ITemplateEngine.class)
public class EmailTemplateAutoConfiguration {

    /**
     * Thymeleaf template renderer.
     * Pluggability rule: backs off when consumer provides own {@link TemplateRenderer}.
     */
    @Bean
    @ConditionalOnMissingBean(TemplateRenderer.class)
    public ThymeleafTemplateRenderer thymeleafTemplateRenderer(ITemplateEngine engine) {
        return new ThymeleafTemplateRenderer(engine);
    }

    /**
     * Thymeleaf template probe for startup validation.
     * Pluggability rule: backs off when consumer provides own {@link TemplateProbe}.
     */
    @Bean
    @ConditionalOnMissingBean(TemplateProbe.class)
    public ThymeleafTemplateProbe thymeleafTemplateProbe(ITemplateEngine engine) {
        return new ThymeleafTemplateProbe(engine);
    }
}
