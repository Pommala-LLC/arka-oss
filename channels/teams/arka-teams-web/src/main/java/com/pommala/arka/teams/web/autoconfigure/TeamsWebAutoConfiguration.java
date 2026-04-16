package com.pommala.arka.teams.web.autoconfigure;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.teams.api.TeamsBatchDispatcher;
import com.pommala.arka.teams.web.internal.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration @ConditionalOnWebApplication
public class TeamsWebAutoConfiguration {
    @Bean public TeamsApiController teamsApiController(MessageFlowService fs, TeamsBatchDispatcher bd) { return new TeamsApiController(fs, bd); }
    @Bean public TeamsExceptionHandler teamsExceptionHandler() { return new TeamsExceptionHandler(); }
    @Bean public TeamsHttpCode teamsHttpCode() { return new TeamsHttpCode(); }
}
