package com.pommala.arka.slack.support.exception;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.slack.support.exception.SlackApplicationException;

/**
 * Thrown when Block Kit message construction fails.
 * Maps to HTTP 422. Not retriable.
 */
public class SlackMessageException extends SlackApplicationException {
    public SlackMessageException(ApplicationCode c, String m) { super(c,m); }
    public SlackMessageException(ApplicationCode c, String m, Throwable t) { super(c,m,t); }
    public SlackMessageException(ApplicationCode c) { super(c); }
}
