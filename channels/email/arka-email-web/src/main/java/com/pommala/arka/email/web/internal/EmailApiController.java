package com.pommala.arka.email.web.internal;

import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.api.MessageFlowService;
import com.pommala.arka.email.api.EmailBatchDispatcher;
import com.pommala.arka.model.BatchDeliveryResult;
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

/**
 * Email channel REST API controller.
 *
 * <p>Tracking ID rule: {@code requestId} is generated here — at the HTTP
 * entry point. Downstream modules read from MDC — they never generate their own.
 */
@RestController
@RequestMapping("/api/v1/email")
public class EmailApiController {

    private static final String LOG_PREFIX = "[arka-email-web]";
    private static final Logger log = LoggerFactory.getLogger(EmailApiController.class);

    private final MessageFlowService flowService;
    private final EmailBatchDispatcher batchDispatcher;

    public EmailApiController(MessageFlowService flowService,
                              EmailBatchDispatcher batchDispatcher) {
        this.flowService = flowService;
        this.batchDispatcher = batchDispatcher;
    }

    /**
     * Submits a single email send request.
     *
     * @param request the email send request
     * @return 200 with delivery confirmation
     */
    @PostMapping("/send")
    public ResponseEntity<EmailSendResponse> send(@Valid @RequestBody EmailSendRequest request) {
        // Tracking ID rule: requestId created once at HTTP entry point.
        // Downstream modules read from MDC — never generate their own.
        var requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} Email send request [flowKey={}, requestId={}]",
                LOG_PREFIX, request.flowKey(), requestId);

        try {
            var command = new EmailSendCommand(
                    request.flowKey(), request.to(), request.cc(),
                    request.bcc(), request.model(), request.subjectOverride());

            var result = flowService.send(command);
            return ResponseEntity.ok(EmailSendResponse.of(result.flowKey(), requestId));
        } finally {
            MDC.remove("requestId");
        }
    }

    /**
     * Submits a batch email send — one email per address, all using the same model.
     *
     * @param request the batch send request
     * @return 200 with batch delivery summary
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchSendResponse> batch(@Valid @RequestBody BatchSendRequest request) {
        var requestId = java.util.UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        log.info("{} Batch send request [flowKey={}, recipients={}, requestId={}]",
                LOG_PREFIX, request.flowKey(), request.addresses().size(), requestId);
        try {
            var result = batchDispatcher.dispatch(
                    request.flowKey(), request.addresses(), request.model()).join();
            return ResponseEntity.ok(BatchSendResponse.of(result, requestId));
        } finally {
            MDC.remove("requestId");
        }
    }

}