package com.pommala.arka.sms.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.UnknownFlowException;

/** Thrown when an SMS flow key cannot be resolved. Maps to HTTP 404. */
public class UnknownSmsFlowException extends UnknownFlowException {
    public UnknownSmsFlowException(ApplicationCode c, String m) { super(c,m); }
    public UnknownSmsFlowException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public UnknownSmsFlowException(ApplicationCode c) { super(c); }
}
