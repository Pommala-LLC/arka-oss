package com.pommala.arka.sms.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/** Thrown when an SMS transport operation fails. Maps to HTTP 502/503/429. */
public class SmsTransportException extends ArkaTransportException {
    public SmsTransportException(ApplicationCode c, String m) { super(c,m); }
    public SmsTransportException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SmsTransportException(ApplicationCode c) { super(c); }
}
