package com.pommala.arka.teams.web.internal;

import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.BusinessRuleViolationException;
import com.pommala.arka.web.ErrorResponse;
import com.pommala.arka.teams.support.exception.*;
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
public class TeamsExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(TeamsExceptionHandler.class);

    @ExceptionHandler(UnknownTeamsFlowException.class)
    public ResponseEntity<ErrorResponse> handleFlowNotFound(UnknownTeamsFlowException ex, HttpServletRequest req) {
        log.debug("[arka-teams-web] Flow not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "TEAMS-HTTP-4002", ex, req);
    }

    @ExceptionHandler(TeamsAuthException.class)
    public ResponseEntity<ErrorResponse> handleAuth(TeamsAuthException ex, HttpServletRequest req) {
        log.error("[arka-teams-web] Auth error: {}", ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "TEAMS-HTTP-5001", ex, req);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleViolationException ex, HttpServletRequest req) {
        log.warn("[arka-teams-web] Business rule violation: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "TEAMS-HTTP-4003", ex, req);
    }

    @ExceptionHandler(TeamsTransportTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(TeamsTransportTimeoutException ex, HttpServletRequest req) {
        log.warn("[arka-teams-web] Transport timeout [code={}]", ex.internalCode().code());
        return build(HttpStatus.GATEWAY_TIMEOUT, "TEAMS-HTTP-5040", ex, req);
    }

    @ExceptionHandler(TeamsTransportException.class)
    public ResponseEntity<ErrorResponse> handleTransport(TeamsTransportException ex, HttpServletRequest req) {
        var status = new TeamsHttpCode().map(ex.internalCode());
        log.error("[arka-teams-web] Transport failure [code={}]", ex.internalCode().code(), ex);
        return build(status, "TEAMS-HTTP-5020", ex, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("[arka-teams-web] Unexpected error", ex);
        return ResponseEntity.status(500).body(new ErrorResponse(false, "TEAMS-HTTP-5099", 500,
                "An unexpected error occurred", "ARKA-CORE-5001", List.of(), req.getRequestURI(), Instant.now().toString()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String httpCode, ArkaApplicationException ex, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ErrorResponse(false, httpCode, status.value(),
                ex.getMessage(), ex.internalCode().code(), List.of(), req.getRequestURI(), Instant.now().toString()));
    }
}
