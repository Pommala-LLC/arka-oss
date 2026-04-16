package com.pommala.arka.sms.web.internal;

import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.BusinessRuleViolationException;
import com.pommala.arka.web.ErrorResponse;
import com.pommala.arka.sms.support.exception.*;
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
public class SmsExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SmsExceptionHandler.class);

    @ExceptionHandler(UnknownSmsFlowException.class)
    public ResponseEntity<ErrorResponse> handleFlowNotFound(UnknownSmsFlowException ex, HttpServletRequest req) {
        log.debug("[arka-sms-web] Flow not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "SMS-HTTP-4002", ex, req);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleViolationException ex, HttpServletRequest req) {
        log.warn("[arka-sms-web] Business rule violation: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "SMS-HTTP-4003", ex, req);
    }

    @ExceptionHandler(SmsTransportTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(SmsTransportTimeoutException ex, HttpServletRequest req) {
        log.warn("[arka-sms-web] Transport timeout [code={}]", ex.internalCode().code());
        return build(HttpStatus.GATEWAY_TIMEOUT, "SMS-HTTP-5040", ex, req);
    }

    @ExceptionHandler(SmsTransportException.class)
    public ResponseEntity<ErrorResponse> handleTransport(SmsTransportException ex, HttpServletRequest req) {
        var status = new SmsHttpCode().map(ex.internalCode());
        log.error("[arka-sms-web] Transport failure [code={}]", ex.internalCode().code(), ex);
        return build(status, "SMS-HTTP-5020", ex, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("[arka-sms-web] Unexpected error", ex);
        return ResponseEntity.status(500).body(new ErrorResponse(false, "SMS-HTTP-5099", 500,
                "An unexpected error occurred", "ARKA-CORE-5001", List.of(), req.getRequestURI(), Instant.now().toString()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String httpCode, ArkaApplicationException ex, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ErrorResponse(false, httpCode, status.value(),
                ex.getMessage(), ex.internalCode().code(), List.of(), req.getRequestURI(), Instant.now().toString()));
    }
}
