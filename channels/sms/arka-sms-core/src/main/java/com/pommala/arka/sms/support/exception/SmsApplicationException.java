package com.pommala.arka.sms.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/** Abstract base for all SMS channel exceptions. */
public abstract class SmsApplicationException extends ArkaApplicationException {
    protected SmsApplicationException(ApplicationCode c, String m) { super(c,m); }
    protected SmsApplicationException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    protected SmsApplicationException(ApplicationCode c) { super(c); }
}
