package com.pommala.arka.whatsapp.web.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.whatsapp.api.WhatsAppBatchDispatcher;
import com.pommala.arka.whatsapp.web.internal.WhatsAppApiController;
import com.pommala.arka.whatsapp.web.internal.WhatsAppExceptionHandler;
import com.pommala.arka.whatsapp.web.internal.WhatsAppHttpCode;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
public class WhatsAppWebAutoConfiguration {

    @Bean
    public WhatsAppApiController whatsAppApiController(MessageFlowService flowService, WhatsAppBatchDispatcher batchDispatcher) {
        return new WhatsAppApiController(flowService, batchDispatcher);
    }

    @Bean
    public WhatsAppExceptionHandler whatsAppExceptionHandler() { return new WhatsAppExceptionHandler(); }

    @Bean
    public WhatsAppHttpCode whatsAppHttpCode() { return new WhatsAppHttpCode(); }
}
