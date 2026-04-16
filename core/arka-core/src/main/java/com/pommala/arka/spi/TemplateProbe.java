package com.pommala.arka.spi;

/**
 * SPI for probing template existence at startup validation time.
 *
 * <p>Contract type: {@code spi} — used by startup validators only.
 * Never called at send time. Default (Thymeleaf) backs off via
 * {@code @ConditionalOnMissingBean}.
 */
public interface TemplateProbe {

    /**
     * Verifies that the named template exists and is resolvable.
     * Throws a typed exception if the template cannot be found.
     *
     * @param templateName the template identifier, never null or blank
     * @throws com.pommala.arka.support.exception.ArkaConfigurationException
     *         if the template cannot be resolved
     */
    void probe(String templateName);
}
