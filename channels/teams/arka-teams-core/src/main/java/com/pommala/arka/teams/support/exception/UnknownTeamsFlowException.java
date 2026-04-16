package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.UnknownFlowException;

/** Thrown when a Teams flow key cannot be resolved. Maps to HTTP 404. */
public class UnknownTeamsFlowException extends UnknownFlowException {
    public UnknownTeamsFlowException(ApplicationCode c, String m) { super(c, m); }
    public UnknownTeamsFlowException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public UnknownTeamsFlowException(ApplicationCode c) { super(c); }
}
