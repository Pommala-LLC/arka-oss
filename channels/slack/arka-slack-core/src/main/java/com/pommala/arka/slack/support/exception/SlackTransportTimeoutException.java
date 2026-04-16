package com.pommala.arka.slack.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.slack.support.exception.SlackTransportException;

/** Thrown when a Slack API request times out. Maps to HTTP 504. Retriable. */
public class SlackTransportTimeoutException extends SlackTransportException {
    public SlackTransportTimeoutException(ApplicationCode c, String m) { super(c,m); }
    public SlackTransportTimeoutException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SlackTransportTimeoutException(ApplicationCode c) { super(c); }
}
