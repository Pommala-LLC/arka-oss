package com.pommala.arka.sms.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.sms.support.exception.SmsApplicationException;

/**
 * Thrown when SMS segment splitting or encoding fails.
 * Maps to HTTP 422. Not retriable.
 */
public class SmsSegmentException extends SmsApplicationException {
    public SmsSegmentException(ApplicationCode c, String m) { super(c,m); }
    public SmsSegmentException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SmsSegmentException(ApplicationCode c) { super(c); }
}
