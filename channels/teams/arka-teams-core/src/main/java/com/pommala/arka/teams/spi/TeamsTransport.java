package com.pommala.arka.teams.spi;

import com.pommala.arka.teams.model.FinalTeamsMessage;
import com.pommala.arka.teams.support.exception.TeamsTransportException;

/**
 * SPI for physical Teams message delivery.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Implementations: incoming webhook or MS Graph API.
 */
public interface TeamsTransport {

    void send(FinalTeamsMessage message);
}
