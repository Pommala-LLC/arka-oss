package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.teams.support.exception.TeamsTransportException;

/**
 * Thrown when a Graph API request times out. Code: {@code TEAMS-TRN-5040}.
 * Maps to HTTP 504. Retriable — transient network condition.
 */
public class TeamsTransportTimeoutException extends TeamsTransportException {
    public TeamsTransportTimeoutException(ApplicationCode c, String m) { super(c, m); }
    public TeamsTransportTimeoutException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsTransportTimeoutException(ApplicationCode c) { super(c); }
}
