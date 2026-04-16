package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.teams.support.exception.TeamsApplicationException;

/**
 * Thrown when Entra ID token acquisition fails or Graph API returns 403.
 * Covers client credential flow failures and insufficient permission grants.
 * Maps to HTTP 502. Not retriable — Entra ID configuration must be corrected.
 *
 * <p>Distinct from {@link TeamsTransportException} because auth failures
 * require operator action on the Entra ID app registration, not just a retry.</p>
 */
public class TeamsAuthException extends TeamsApplicationException {
    public TeamsAuthException(ApplicationCode c, String m) { super(c, m); }
    public TeamsAuthException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsAuthException(ApplicationCode c) { super(c); }
}
