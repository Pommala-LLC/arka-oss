package com.pommala.arka.slack.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaTransportException;

/** Thrown when a Slack API or webhook transport operation fails. Maps to HTTP 502/503/429. */
public class SlackTransportException extends ArkaTransportException {
    public SlackTransportException(ApplicationCode c, String m) { super(c,m); }
    public SlackTransportException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SlackTransportException(ApplicationCode c) { super(c); }
}
