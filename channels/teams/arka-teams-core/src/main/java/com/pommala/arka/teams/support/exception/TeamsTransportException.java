package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/**
 * Thrown when a Microsoft Graph API transport operation fails.
 *
 * <p>HTTP mapping is code-based:</p>
 * <ul>
 *   <li>{@code TEAMS-TRN-5031} → 503 (Graph unavailable)</li>
 *   <li>{@code TEAMS-TRN-5040} → 504 (via {@link TeamsTransportTimeoutException})</li>
 *   <li>{@code TEAMS-TRN-5050} → 429 (throttled — Retry-After applies)</li>
 *   <li>All others → 502</li>
 * </ul>
 *
 * <p>All low-level Graph SDK exceptions must be translated by
 * {@code GraphExceptionTranslator} before leaving the transport layer.</p>
 */
public class TeamsTransportException extends ArkaTransportException {
    public TeamsTransportException(ApplicationCode c, String m) { super(c, m); }
    public TeamsTransportException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsTransportException(ApplicationCode c) { super(c); }
}
