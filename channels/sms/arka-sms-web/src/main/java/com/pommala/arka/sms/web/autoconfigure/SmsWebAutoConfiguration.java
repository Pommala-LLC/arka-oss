package com.pommala.arka.sms.web.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.sms.api.SmsBatchDispatcher;
import com.pommala.arka.sms.web.internal.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration @ConditionalOnWebApplication
public class SmsWebAutoConfiguration {
    @Bean public SmsApiController smsApiController(MessageFlowService fs, SmsBatchDispatcher bd) { return new SmsApiController(fs, bd); }
    @Bean public SmsExceptionHandler smsExceptionHandler() { return new SmsExceptionHandler(); }
    @Bean public SmsHttpCode smsHttpCode() { return new SmsHttpCode(); }
}
