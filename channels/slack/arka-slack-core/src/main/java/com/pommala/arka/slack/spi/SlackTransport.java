package com.pommala.arka.slack.spi;

import com.pommala.arka.slack.model.FinalSlackMessage;
import com.pommala.arka.slack.support.exception.SlackTransportException;

/**
 * SPI for physical Slack message delivery.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Implementations: webhook transport (incoming webhooks) or API transport (Web API).
 */
public interface SlackTransport {

    void send(FinalSlackMessage message);
}
