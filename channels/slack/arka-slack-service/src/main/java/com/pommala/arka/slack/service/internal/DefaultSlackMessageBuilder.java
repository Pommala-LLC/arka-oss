package com.pommala.arka.slack.service.internal;

import com.pommala.arka.slack.api.SlackSendCommand;
import com.pommala.arka.slack.model.FinalSlackMessage;
import com.pommala.arka.slack.model.ResolvedSlackFlow;
import com.pommala.arka.slack.spi.SlackMessageBuilder;

public class DefaultSlackMessageBuilder implements SlackMessageBuilder {

    @Override
    public FinalSlackMessage build(ResolvedSlackFlow flow, SlackSendCommand command) {
        var text = command.textOverride() != null ? command.textOverride() : resolveText(flow, command);
        return new FinalSlackMessage(
                command.channelId() != null ? command.channelId() : flow.defaultChannel(),
                text, null, command.threadTs(),
                flow.webhookUrl(), flow.botToken(), flow.transportMode());
    }

    private String resolveText(ResolvedSlackFlow flow, SlackSendCommand command) {
        if (flow.template() == null) return "";
        var text = flow.template();
        for (var entry : command.model().entrySet())
            text = text.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        return text;
    }
}
