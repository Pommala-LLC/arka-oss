package com.pommala.arka.slack.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.UnknownFlowException;

/** Thrown when a Slack flow key cannot be resolved. Maps to HTTP 404. */
public class UnknownSlackFlowException extends UnknownFlowException {
    public UnknownSlackFlowException(ApplicationCode c, String m) { super(c,m); }
    public UnknownSlackFlowException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public UnknownSlackFlowException(ApplicationCode c) { super(c); }
}
