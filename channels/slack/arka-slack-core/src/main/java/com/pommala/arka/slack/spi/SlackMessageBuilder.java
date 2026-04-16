package com.pommala.arka.slack.spi;

import com.pommala.arka.slack.api.SlackSendCommand;
import com.pommala.arka.slack.model.FinalSlackMessage;
import com.pommala.arka.slack.model.ResolvedSlackFlow;

/**
 * SPI for constructing the send-ready Slack message.
 */
public interface SlackMessageBuilder {

    FinalSlackMessage build(ResolvedSlackFlow flow, SlackSendCommand command);
}
