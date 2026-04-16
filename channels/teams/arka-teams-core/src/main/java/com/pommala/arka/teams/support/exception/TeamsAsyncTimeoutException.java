package com.pommala.arka.teams.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaAsyncTimeoutException;

/** Thrown when Teams async dispatch exceeds its deadline. Maps to HTTP 504. */
public class TeamsAsyncTimeoutException extends ArkaAsyncTimeoutException {
    public TeamsAsyncTimeoutException(ApplicationCode c, String m) { super(c, m); }
    public TeamsAsyncTimeoutException(ApplicationCode c, String m, Throwable t) { super(c, m, t); }
    public TeamsAsyncTimeoutException(ApplicationCode c) { super(c); }
}
