package com.pommala.arka.email.web.internal;

import com.pommala.arka.web.AppCodeHttpMapper;
import com.pommala.arka.email.support.code.EmailTransportCode;
import com.pommala.arka.support.code.ApplicationCode;
import org.springframework.http.HttpStatus;

/**
 * Maps email internal application codes to HTTP statuses.
 *
 * <p>Transport code mapping (code-based, not type-based):</p>
 * <ul>
 *   <li>{@code EMAIL-TRN-5031} → 503 (subsystem temporarily unavailable)</li>
 *   <li>{@code EMAIL-TRN-5040} → 504 (timeout — via subtype)</li>
 *   <li>All other transport codes → 502</li>
 * </ul>
 */
public class EmailHttpCode implements AppCodeHttpMapper {

    @Override
    public HttpStatus map(ApplicationCode code) {
        if (code instanceof EmailTransportCode tc) {
            return switch (tc) {
                case SMTP_SUBSYSTEM_ERROR -> HttpStatus.SERVICE_UNAVAILABLE; // 503
                case SMTP_TIMEOUT        -> HttpStatus.GATEWAY_TIMEOUT;      // 504
                default                  -> HttpStatus.BAD_GATEWAY;          // 502
            };
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
