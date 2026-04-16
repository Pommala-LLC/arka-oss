package com.pommala.arka.email.web.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.email.api.EmailBatchDispatcher;
import com.pommala.arka.email.web.internal.EmailApiController;
import com.pommala.arka.email.web.internal.EmailExceptionHandler;
import com.pommala.arka.email.web.internal.EmailHttpCode;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for the email channel HTTP surface. */
@AutoConfiguration
@ConditionalOnWebApplication
public class EmailWebAutoConfiguration {

    @Bean
    public EmailApiController emailApiController(MessageFlowService flowService,
                                                 EmailBatchDispatcher batchDispatcher) {
        return new EmailApiController(flowService, batchDispatcher);
    }

    @Bean
    public EmailExceptionHandler emailExceptionHandler() {
        return new EmailExceptionHandler();
    }

    @Bean
    public EmailHttpCode emailHttpCode() {
        return new EmailHttpCode();
    }
}
