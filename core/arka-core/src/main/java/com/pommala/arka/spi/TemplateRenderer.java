package com.pommala.arka.spi;

import java.util.Map;

/**
 * SPI for template rendering.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean per channel.
 * Default (Thymeleaf) backs off via {@code @ConditionalOnMissingBean}.
 */
public interface TemplateRenderer {

    /**
     * Renders the named template with the given model variables.
     *
     * @param templateName the template identifier, never null or blank
     * @param variables    model variables, never null (may be empty)
     * @return the rendered output, never null
     */
    String render(String templateName, Map<String, Object> variables);
}
