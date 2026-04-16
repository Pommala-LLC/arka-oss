package com.pommala.arka.email.web.internal;

import com.pommala.arka.support.exception.BusinessRuleViolationException;
import com.pommala.arka.email.support.exception.EmailConfigurationException;
import com.pommala.arka.email.support.exception.EmailTransportException;
import com.pommala.arka.email.support.exception.EmailTransportTimeoutException;
import com.pommala.arka.email.support.exception.InvalidEmailRequestException;
import com.pommala.arka.email.support.exception.InvalidRecipientException;
import com.pommala.arka.email.support.exception.UnknownEmailFlowException;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.web.ErrorResponse;
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
import com.pommala.arka.email.web.internal.EmailHttpCode;

/**
 * Email channel exception handler.
 *
 * <p>Handler logging rules by HTTP range:</p>
 * <ul>
 *   <li>400 — no log</li>
 *   <li>404 — DEBUG</li>
 *   <li>422 — WARN</li>
 *   <li>500 — ERROR with stack trace</li>
 *   <li>502 — ERROR (transport logged root cause)</li>
 *   <li>503 — WARN (transient)</li>
 *   <li>504 — WARN (transient)</li>
 * </ul>
 */
@RestControllerAdvice
public class EmailExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(EmailExceptionHandler.class);

    @ExceptionHandler(InvalidEmailRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(
            InvalidEmailRequestException ex, HttpServletRequest req) {
        // 400 — no log (client error)
        return build(HttpStatus.BAD_REQUEST, "EMAIL-HTTP-4000", ex, req);
    }

    @ExceptionHandler(InvalidRecipientException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRecipient(
            InvalidRecipientException ex, HttpServletRequest req) {
        // 400 — no log (client data problem)
        return build(HttpStatus.BAD_REQUEST, "EMAIL-HTTP-4001", ex, req);
    }

    @ExceptionHandler(UnknownEmailFlowException.class)
    public ResponseEntity<ErrorResponse> handleFlowNotFound(
            UnknownEmailFlowException ex, HttpServletRequest req) {
        log.debug("[arka-email-web] Flow not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "EMAIL-HTTP-4002", ex, req);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleViolationException ex, HttpServletRequest req) {
        log.warn("[arka-email-web] Business rule violation: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "EMAIL-HTTP-4003", ex, req);
    }

    @ExceptionHandler(EmailTransportTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(
            EmailTransportTimeoutException ex, HttpServletRequest req) {
        log.warn("[arka-email-web] Transport timeout [code={}]", ex.internalCode().code());
        return build(HttpStatus.GATEWAY_TIMEOUT, "EMAIL-HTTP-5040", ex, req);
    }

    @ExceptionHandler(EmailTransportException.class)
    public ResponseEntity<ErrorResponse> handleTransport(
            EmailTransportException ex, HttpServletRequest req) {
        var httpCode = new EmailHttpCode();
        var status   = httpCode.map(ex.internalCode());
        if (status == HttpStatus.SERVICE_UNAVAILABLE) {
            log.warn("[arka-email-web] Transport subsystem unavailable [code={}]",
                    ex.internalCode().code());
        } else {
            log.error("[arka-email-web] Transport failure [code={}]",
                    ex.internalCode().code(), ex);
        }
        return build(status, "EMAIL-HTTP-5020", ex, req);
    }

    @ExceptionHandler(EmailConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleConfiguration(
            EmailConfigurationException ex, HttpServletRequest req) {
        log.error("[arka-email-web] Configuration error [category={}, code={}]",
                ex.configCategory(), ex.internalCode().code(), ex);
        // Configuration category → HTTP code mapping (exhaustive switch, no default).
        var httpCode = switch (ex.configCategory()) {
            case FLOW       -> "EMAIL-HTTP-5004";
            case SENDER     -> "EMAIL-HTTP-5002";
            case ATTACHMENT -> "EMAIL-HTTP-5003";
            case TEMPLATE   -> "EMAIL-HTTP-5005";
            case MESSAGE    -> "EMAIL-HTTP-5005";
        };
        return build(HttpStatus.INTERNAL_SERVER_ERROR, httpCode, ex, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorDetail(fe.getField(),
                        String.valueOf(fe.getRejectedValue()), fe.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(false, "EMAIL-HTTP-4000",
                        HttpStatus.BAD_REQUEST.value(),
                        "Request validation failed", "EMAIL-VAL-4000",
                        details, req.getRequestURI(), Instant.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("[arka-email-web] Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(false, "EMAIL-HTTP-5099",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred", "ARKA-CORE-5001",
                        List.of(), req.getRequestURI(), Instant.now().toString()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String httpCode,
                                                  ArkaApplicationException ex,
                                                  HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(false, httpCode, status.value(),
                        ex.getMessage(), ex.internalCode().code(),
                        List.of(), req.getRequestURI(), Instant.now().toString()));
    }
}
