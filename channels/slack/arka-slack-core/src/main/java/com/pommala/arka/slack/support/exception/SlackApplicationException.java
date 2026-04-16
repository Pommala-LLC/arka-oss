package com.pommala.arka.slack.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.support.exception.ArkaApplicationException;

/** Abstract base for all Slack channel exceptions. */
public abstract class SlackApplicationException extends ArkaApplicationException {
    protected SlackApplicationException(ApplicationCode c, String m) { super(c,m); }
    protected SlackApplicationException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    protected SlackApplicationException(ApplicationCode c) { super(c); }
}
