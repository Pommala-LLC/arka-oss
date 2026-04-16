package com.pommala.arka.teams.spi;

import com.pommala.arka.teams.api.TeamsSendCommand;
import com.pommala.arka.teams.model.FinalTeamsMessage;
import com.pommala.arka.teams.model.ResolvedTeamsFlow;

/**
 * SPI for constructing the send-ready Teams message.
 */
public interface TeamsMessageBuilder {

    FinalTeamsMessage build(ResolvedTeamsFlow flow, TeamsSendCommand command);
}
