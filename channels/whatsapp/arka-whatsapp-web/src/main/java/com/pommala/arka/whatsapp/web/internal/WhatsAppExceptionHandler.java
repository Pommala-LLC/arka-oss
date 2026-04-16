package com.pommala.arka.whatsapp.web.internal;

import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.BusinessRuleViolationException;
import com.pommala.arka.web.ErrorResponse;
import com.pommala.arka.whatsapp.support.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WhatsAppExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppExceptionHandler.class);

    @ExceptionHandler(UnknownWhatsAppFlowException.class)
    public ResponseEntity<ErrorResponse> handleFlowNotFound(UnknownWhatsAppFlowException ex, HttpServletRequest req) {
        log.debug("[arka-whatsapp-web] Flow not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "WA-HTTP-4002", ex, req);
    }

    @ExceptionHandler(WhatsAppTemplateException.class)
    public ResponseEntity<ErrorResponse> handleTemplate(WhatsAppTemplateException ex, HttpServletRequest req) {
        log.warn("[arka-whatsapp-web] Template error: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "WA-HTTP-4220", ex, req);
    }

    @ExceptionHandler(WhatsAppSessionExpiredException.class)
    public ResponseEntity<ErrorResponse> handleSessionExpired(WhatsAppSessionExpiredException ex, HttpServletRequest req) {
        log.warn("[arka-whatsapp-web] Session expired: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "WA-HTTP-4221", ex, req);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleViolationException ex, HttpServletRequest req) {
        log.warn("[arka-whatsapp-web] Business rule violation: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "WA-HTTP-4003", ex, req);
    }

    @ExceptionHandler(WhatsAppTransportTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(WhatsAppTransportTimeoutException ex, HttpServletRequest req) {
        log.warn("[arka-whatsapp-web] Transport timeout [code={}]", ex.internalCode().code());
        return build(HttpStatus.GATEWAY_TIMEOUT, "WA-HTTP-5040", ex, req);
    }

    @ExceptionHandler(WhatsAppTransportException.class)
    public ResponseEntity<ErrorResponse> handleTransport(WhatsAppTransportException ex, HttpServletRequest req) {
        var status = new WhatsAppHttpCode().map(ex.internalCode());
        log.error("[arka-whatsapp-web] Transport failure [code={}]", ex.internalCode().code(), ex);
        return build(status, "WA-HTTP-5020", ex, req);
    }

    @ExceptionHandler(WhatsAppConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleConfiguration(WhatsAppConfigurationException ex, HttpServletRequest req) {
        log.error("[arka-whatsapp-web] Configuration error [category={}, code={}]", ex.configCategory(), ex.internalCode().code(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "WA-HTTP-5000", ex, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorDetail(fe.getField(), String.valueOf(fe.getRejectedValue()), fe.getDefaultMessage())).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(false, "WA-HTTP-4000", 400, "Request validation failed", "WA-VAL-4000", details, req.getRequestURI(), Instant.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("[arka-whatsapp-web] Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(false, "WA-HTTP-5099", 500, "An unexpected error occurred", "ARKA-CORE-5001", List.of(), req.getRequestURI(), Instant.now().toString()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String httpCode, ArkaApplicationException ex, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(false, httpCode, status.value(), ex.getMessage(), ex.internalCode().code(), List.of(), req.getRequestURI(), Instant.now().toString()));
    }
}
