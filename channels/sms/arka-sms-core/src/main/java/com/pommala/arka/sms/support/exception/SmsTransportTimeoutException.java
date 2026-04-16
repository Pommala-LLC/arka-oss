package com.pommala.arka.sms.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.sms.support.exception.SmsTransportException;

/** Thrown when an SMS API request times out. Maps to HTTP 504. Retriable. */
public class SmsTransportTimeoutException extends SmsTransportException {
    public SmsTransportTimeoutException(ApplicationCode c, String m) { super(c,m); }
    public SmsTransportTimeoutException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SmsTransportTimeoutException(ApplicationCode c) { super(c); }
}
