package com.pommala.arka.email.template.thymeleaf.internal;

import com.pommala.arka.email.support.code.EmailConfigCategory;
import com.pommala.arka.email.support.code.EmailTemplateCode;
import com.pommala.arka.email.support.exception.EmailConfigurationException;
import com.pommala.arka.spi.TemplateProbe;
import java.util.Objects;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Thymeleaf template existence probe for startup validation.
 *
 * <p>Called at startup only — never at send time.
 * Verifies that each template referenced in flow configuration exists
 * and is resolvable by Thymeleaf.
 */
public class ThymeleafTemplateProbe implements TemplateProbe {

    private final ITemplateEngine templateEngine;

    public ThymeleafTemplateProbe(ITemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine);
    }

    @Override
    public void probe(String templateName) {
        Objects.requireNonNull(templateName, "templateName must not be null");
        try {
            // Attempt to process the template with empty context to verify it exists.
            // Toggle-dependent: only called when validate-template-exists is enabled.
            templateEngine.process(templateName, new Context());
        } catch (Exception ex) {
            throw new EmailConfigurationException(
                    EmailTemplateCode.TEMPLATE_NOT_RESOLVABLE,
                    EmailConfigCategory.TEMPLATE,
                    "Template not resolvable at startup: " + templateName, ex);
        }
    }
}
