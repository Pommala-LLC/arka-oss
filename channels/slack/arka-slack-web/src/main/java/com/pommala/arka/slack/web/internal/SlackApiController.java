package com.pommala.arka.slack.web.internal;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.slack.api.SlackBatchDispatcher;
import com.pommala.arka.slack.api.SlackSendCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/slack")
public class SlackApiController {
    private static final String LOG_PREFIX = "[arka-slack-web]";
    private static final Logger log = LoggerFactory.getLogger(SlackApiController.class);
    private final MessageFlowService flowService;
    private final SlackBatchDispatcher batchDispatcher;
    public SlackApiController(MessageFlowService flowService, SlackBatchDispatcher batchDispatcher) {
        this.flowService = flowService; this.batchDispatcher = batchDispatcher;
    }

    @PostMapping("/send")
    public ResponseEntity<SlackSendResponse> send(@Valid @RequestBody SlackSendRequest request) {
        var requestId = UUID.randomUUID().toString(); MDC.put("requestId", requestId);
        log.info("{} Slack send [flowKey={}, requestId={}]", LOG_PREFIX, request.flowKey(), requestId);
        try {
            var command = new SlackSendCommand(request.flowKey(), request.channelId(), request.model(), request.textOverride(), request.threadTs());
            var result = flowService.send(command);
            return ResponseEntity.ok(SlackSendResponse.of(result.flowKey(), requestId));
        } finally { MDC.remove("requestId"); }
    }

    @PostMapping("/batch")
    public ResponseEntity<SlackBatchSendResponse> batch(@Valid @RequestBody SlackBatchSendRequest request) {
        var requestId = UUID.randomUUID().toString(); MDC.put("requestId", requestId);
        log.info("{} Batch send [flowKey={}, targets={}, requestId={}]", LOG_PREFIX, request.flowKey(), request.channelIds().size(), requestId);
        try {
            var result = batchDispatcher.dispatch(request.flowKey(), request.channelIds(), request.model()).join();
            return ResponseEntity.ok(SlackBatchSendResponse.of(result, requestId));
        } finally { MDC.remove("requestId"); }
    }
}
