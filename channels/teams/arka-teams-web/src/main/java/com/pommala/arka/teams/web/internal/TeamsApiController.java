package com.pommala.arka.teams.web.internal;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.teams.api.TeamsBatchDispatcher;
import com.pommala.arka.teams.api.TeamsSendCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/teams")
public class TeamsApiController {
    private static final String LOG_PREFIX = "[arka-teams-web]";
    private static final Logger log = LoggerFactory.getLogger(TeamsApiController.class);
    private final MessageFlowService flowService;
    private final TeamsBatchDispatcher batchDispatcher;
    public TeamsApiController(MessageFlowService flowService, TeamsBatchDispatcher batchDispatcher) {
        this.flowService = flowService; this.batchDispatcher = batchDispatcher;
    }

    @PostMapping("/send")
    public ResponseEntity<TeamsSendResponse> send(@Valid @RequestBody TeamsSendRequest request) {
        var requestId = UUID.randomUUID().toString(); MDC.put("requestId", requestId);
        log.info("{} Teams send [flowKey={}, requestId={}]", LOG_PREFIX, request.flowKey(), requestId);
        try {
            var command = new TeamsSendCommand(request.flowKey(), request.channelId(), request.model(), request.textOverride(), request.replyToId());
            var result = flowService.send(command);
            return ResponseEntity.ok(TeamsSendResponse.of(result.flowKey(), requestId));
        } finally { MDC.remove("requestId"); }
    }

    @PostMapping("/batch")
    public ResponseEntity<TeamsBatchSendResponse> batch(@Valid @RequestBody TeamsBatchSendRequest request) {
        var requestId = UUID.randomUUID().toString(); MDC.put("requestId", requestId);
        log.info("{} Batch send [flowKey={}, targets={}, requestId={}]", LOG_PREFIX, request.flowKey(), request.channelIds().size(), requestId);
        try {
            var result = batchDispatcher.dispatch(request.flowKey(), request.channelIds(), request.model()).join();
            return ResponseEntity.ok(TeamsBatchSendResponse.of(result, requestId));
        } finally { MDC.remove("requestId"); }
    }
}
