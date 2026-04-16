package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/** Abstract base for all Teams channel exceptions. */
public abstract class TeamsApplicationException extends ArkaApplicationException {
    protected TeamsApplicationException(ApplicationCode c, String m) { super(c, m); }
    protected TeamsApplicationException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    protected TeamsApplicationException(ApplicationCode c) { super(c); }
}
