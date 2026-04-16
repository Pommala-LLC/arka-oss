package com.pommala.arka.sms.web.internal;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.sms.api.SmsBatchDispatcher;
import com.pommala.arka.sms.api.SmsSendCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsApiController {
    private static final String LOG_PREFIX = "[arka-sms-web]";
    private static final Logger log = LoggerFactory.getLogger(SmsApiController.class);
    private final MessageFlowService flowService;
    private final SmsBatchDispatcher batchDispatcher;

    public SmsApiController(MessageFlowService flowService, SmsBatchDispatcher batchDispatcher) {
        this.flowService = flowService; this.batchDispatcher = batchDispatcher;
    }

    @PostMapping("/send")
    public ResponseEntity<SmsSendResponse> send(@Valid @RequestBody SmsSendRequest request) {
        var requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} SMS send request [flowKey={}, requestId={}]", LOG_PREFIX, request.flowKey(), requestId);
        try {
            var command = new SmsSendCommand(request.flowKey(), request.to(), request.model(), request.bodyOverride(), request.fromOverride());
            var result = flowService.send(command);
            return ResponseEntity.ok(SmsSendResponse.of(result.flowKey(), requestId));
        } finally { MDC.remove("requestId"); }
    }

    @PostMapping("/batch")
    public ResponseEntity<SmsBatchSendResponse> batch(@Valid @RequestBody SmsBatchSendRequest request) {
        var requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} Batch send [flowKey={}, recipients={}, requestId={}]", LOG_PREFIX, request.flowKey(), request.phoneNumbers().size(), requestId);
        try {
            var result = batchDispatcher.dispatch(request.flowKey(), request.phoneNumbers(), request.model()).join();
            return ResponseEntity.ok(SmsBatchSendResponse.of(result, requestId));
        } finally { MDC.remove("requestId"); }
    }
}
