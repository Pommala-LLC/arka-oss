package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.teams.support.exception.TeamsApplicationException;

/**
 * Thrown when Adaptive Card construction or schema validation fails.
 * Maps to HTTP 422. Not retriable — card template or variables must be corrected.
 */
public class TeamsMessageException extends TeamsApplicationException {
    public TeamsMessageException(ApplicationCode c, String m) { super(c, m); }
    public TeamsMessageException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsMessageException(ApplicationCode c) { super(c); }
}
