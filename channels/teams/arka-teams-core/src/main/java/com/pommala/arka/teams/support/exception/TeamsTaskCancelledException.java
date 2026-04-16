package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTaskCancelledException;

/** Thrown when a Teams virtual thread task is cancelled. Maps to HTTP 503. */
public class TeamsTaskCancelledException extends ArkaTaskCancelledException {
    public TeamsTaskCancelledException(ApplicationCode c, String m) { super(c, m); }
    public TeamsTaskCancelledException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsTaskCancelledException(ApplicationCode c) { super(c); }
}
