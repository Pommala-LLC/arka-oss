package com.pommala.arka.email.template.thymeleaf.internal;

import com.pommala.arka.email.support.code.EmailConfigCategory;
import com.pommala.arka.email.support.code.EmailTemplateCode;
import com.pommala.arka.email.support.exception.EmailConfigurationException;
import com.pommala.arka.spi.TemplateRenderer;
import java.util.Map;
import java.util.Objects;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Thymeleaf-based template renderer.
 *
 * <p>All Thymeleaf exceptions are translated to typed
 * {@link EmailConfigurationException} before leaving this class.
 */
public class ThymeleafTemplateRenderer implements TemplateRenderer {

    private final ITemplateEngine templateEngine;

    public ThymeleafTemplateRenderer(ITemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine);
    }

    @Override
    public String render(String templateName, Map<String, Object> variables) {
        Objects.requireNonNull(templateName, "templateName must not be null");
        Objects.requireNonNull(variables,    "variables must not be null");

        try {
            var context = new Context();
            context.setVariables(variables);
            return templateEngine.process(templateName, context);
        } catch (org.thymeleaf.exceptions.TemplateInputException ex) {
            throw new EmailConfigurationException(
                    EmailTemplateCode.TEMPLATE_NOT_RESOLVABLE,
                    EmailConfigCategory.TEMPLATE,
                    "Template not resolvable: " + templateName, ex);
        } catch (Exception ex) {
            throw new EmailConfigurationException(
                    EmailTemplateCode.TEMPLATE_RENDER_FAILED,
                    EmailConfigCategory.TEMPLATE,
                    "Template rendering failed: " + templateName, ex);
        }
    }
}
