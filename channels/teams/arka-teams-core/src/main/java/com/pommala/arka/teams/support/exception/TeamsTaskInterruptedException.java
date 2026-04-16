package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTaskInterruptedException;

/**
 * Thrown when a Teams virtual thread task is interrupted. Maps to HTTP 503.
 * Always call {@code Thread.currentThread().interrupt()} before throwing.
 */
public class TeamsTaskInterruptedException extends ArkaTaskInterruptedException {
    public TeamsTaskInterruptedException(ApplicationCode c, String m) { super(c, m); }
    public TeamsTaskInterruptedException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsTaskInterruptedException(ApplicationCode c) { super(c); }
}
