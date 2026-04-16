package com.pommala.arka.whatsapp.service.internal;

import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import com.pommala.arka.whatsapp.model.FinalWhatsAppMessage;
import com.pommala.arka.whatsapp.model.ResolvedWhatsAppFlow;
import com.pommala.arka.whatsapp.spi.WhatsAppMessageBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Default WhatsApp message builder.
 *
 * <p>Constructs a {@link FinalWhatsAppMessage} from the resolved flow and command.
 * For template mode, extracts ordered parameters from the model map.
 * For session mode, resolves body text from command override or flow default.
 */
public class DefaultWhatsAppMessageBuilder implements WhatsAppMessageBuilder {

    @Override
    public FinalWhatsAppMessage build(ResolvedWhatsAppFlow flow, WhatsAppSendCommand command) {
        var templateParams = List.<String>of();
        var body = (String) null;

        if (flow.isTemplateMode()) {
            templateParams = extractOrderedParams(command.model());
        } else {
            body = command.bodyOverride() != null ? command.bodyOverride() : flow.defaultBody();
        }

        var templateName = command.templateNameOverride() != null
                ? command.templateNameOverride() : flow.templateName();

        return new FinalWhatsAppMessage(
                command.to(),
                flow.phoneNumberId(),
                flow.accessToken(),
                flow.deliveryMode(),
                templateName,
                flow.languageCode(),
                templateParams,
                body,
                command.mediaUrl(),
                Map.of());
    }

    private List<String> extractOrderedParams(Map<String, Object> model) {
        var params = new ArrayList<String>();
        for (int i = 1; i <= model.size(); i++) {
            var key = String.valueOf(i);
            var altKey = "param" + i;
            var value = model.containsKey(key) ? model.get(key) : model.get(altKey);
            if (value != null) {
                params.add(String.valueOf(value));
            }
        }
        if (params.isEmpty()) {
            model.values().stream()
                    .map(String::valueOf)
                    .forEach(params::add);
        }
        return params;
    }
}
