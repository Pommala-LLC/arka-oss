package com.pommala.arka.slack.web.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.slack.api.SlackBatchDispatcher;
import com.pommala.arka.slack.web.internal.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration @ConditionalOnWebApplication
public class SlackWebAutoConfiguration {
    @Bean public SlackApiController slackApiController(MessageFlowService fs, SlackBatchDispatcher bd) { return new SlackApiController(fs, bd); }
    @Bean public SlackExceptionHandler slackExceptionHandler() { return new SlackExceptionHandler(); }
    @Bean public SlackHttpCode slackHttpCode() { return new SlackHttpCode(); }
}
