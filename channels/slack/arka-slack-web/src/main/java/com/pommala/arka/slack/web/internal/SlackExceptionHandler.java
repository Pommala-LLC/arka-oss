package com.pommala.arka.slack.web.internal;

import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.BusinessRuleViolationException;
import com.pommala.arka.web.ErrorResponse;
import com.pommala.arka.slack.support.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SlackExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SlackExceptionHandler.class);

    @ExceptionHandler(UnknownSlackFlowException.class)
    public ResponseEntity<ErrorResponse> handleFlowNotFound(UnknownSlackFlowException ex, HttpServletRequest req) {
        log.debug("[arka-slack-web] Flow not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "SLACK-HTTP-4002", ex, req);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleViolationException ex, HttpServletRequest req) {
        log.warn("[arka-slack-web] Business rule violation: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "SLACK-HTTP-4003", ex, req);
    }

    @ExceptionHandler(SlackTransportTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(SlackTransportTimeoutException ex, HttpServletRequest req) {
        log.warn("[arka-slack-web] Transport timeout [code={}]", ex.internalCode().code());
        return build(HttpStatus.GATEWAY_TIMEOUT, "SLACK-HTTP-5040", ex, req);
    }

    @ExceptionHandler(SlackTransportException.class)
    public ResponseEntity<ErrorResponse> handleTransport(SlackTransportException ex, HttpServletRequest req) {
        var status = new SlackHttpCode().map(ex.internalCode());
        log.error("[arka-slack-web] Transport failure [code={}]", ex.internalCode().code(), ex);
        return build(status, "SLACK-HTTP-5020", ex, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("[arka-slack-web] Unexpected error", ex);
        return ResponseEntity.status(500).body(new ErrorResponse(false, "SLACK-HTTP-5099", 500,
                "An unexpected error occurred", "ARKA-CORE-5001", List.of(), req.getRequestURI(), Instant.now().toString()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String httpCode, ArkaApplicationException ex, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ErrorResponse(false, httpCode, status.value(),
                ex.getMessage(), ex.internalCode().code(), List.of(), req.getRequestURI(), Instant.now().toString()));
    }
}
