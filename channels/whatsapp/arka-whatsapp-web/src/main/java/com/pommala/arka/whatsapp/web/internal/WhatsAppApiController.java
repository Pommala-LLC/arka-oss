package com.pommala.arka.whatsapp.web.internal;

import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.whatsapp.api.WhatsAppBatchDispatcher;
import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/whatsapp")
public class WhatsAppApiController {

    private static final String LOG_PREFIX = "[arka-whatsapp-web]";
    private static final Logger log = LoggerFactory.getLogger(WhatsAppApiController.class);

    private final MessageFlowService flowService;
    private final WhatsAppBatchDispatcher batchDispatcher;

    public WhatsAppApiController(MessageFlowService flowService, WhatsAppBatchDispatcher batchDispatcher) {
        this.flowService = flowService;
        this.batchDispatcher = batchDispatcher;
    }

    @PostMapping("/send")
    public ResponseEntity<WhatsAppSendResponse> send(@Valid @RequestBody WhatsAppSendRequest request) {
        var requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} WhatsApp send request [flowKey={}, requestId={}]", LOG_PREFIX, request.flowKey(), requestId);
        try {
            var command = new WhatsAppSendCommand(request.flowKey(), request.to(), request.model(),
                    request.templateNameOverride(), request.mediaUrl(), request.bodyOverride());
            var result = flowService.send(command);
            return ResponseEntity.ok(WhatsAppSendResponse.of(result.flowKey(), requestId));
        } finally { MDC.remove("requestId"); }
    }

    @PostMapping("/batch")
    public ResponseEntity<WhatsAppBatchSendResponse> batch(@Valid @RequestBody WhatsAppBatchSendRequest request) {
        var requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} Batch send request [flowKey={}, recipients={}, requestId={}]", LOG_PREFIX, request.flowKey(), request.phoneNumbers().size(), requestId);
        try {
            var result = batchDispatcher.dispatch(request.flowKey(), request.phoneNumbers(), request.model()).join();
            return ResponseEntity.ok(WhatsAppBatchSendResponse.of(result, requestId));
        } finally { MDC.remove("requestId"); }
    }
}
