package com.pommala.arka.teams.service.internal;

import com.pommala.arka.teams.api.TeamsSendCommand;
import com.pommala.arka.teams.model.FinalTeamsMessage;
import com.pommala.arka.teams.model.ResolvedTeamsFlow;
import com.pommala.arka.teams.spi.TeamsMessageBuilder;

public class DefaultTeamsMessageBuilder implements TeamsMessageBuilder {
    @Override
    public FinalTeamsMessage build(ResolvedTeamsFlow flow, TeamsSendCommand command) {
        var text = command.textOverride() != null ? command.textOverride() : resolveText(flow, command);
        var channelId = command.channelId() != null ? command.channelId() : flow.defaultChannel();
        return new FinalTeamsMessage(channelId, text, null, command.replyToId(),
                flow.webhookUrl(), flow.tenantId(), flow.clientId(), flow.clientSecret(), flow.transportMode());
    }
    private String resolveText(ResolvedTeamsFlow flow, TeamsSendCommand command) {
        if (flow.template() == null) return "";
        var text = flow.template();
        for (var entry : command.model().entrySet()) text = text.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        return text;
    }
}
