package com.pommala.arka.sms.service.internal;

import com.pommala.arka.sms.api.SmsSendCommand;
import com.pommala.arka.sms.model.FinalSmsMessage;
import com.pommala.arka.sms.model.ResolvedSmsFlow;
import com.pommala.arka.sms.spi.SmsMessageBuilder;

public class DefaultSmsMessageBuilder implements SmsMessageBuilder {

    @Override
    public FinalSmsMessage build(ResolvedSmsFlow flow, SmsSendCommand command) {
        var body = command.bodyOverride() != null ? command.bodyOverride() : resolveBody(flow, command);
        var from = command.fromOverride() != null ? command.fromOverride() : flow.fromNumber();
        return new FinalSmsMessage(command.to(), from, body, flow.messageType());
    }

    private String resolveBody(ResolvedSmsFlow flow, SmsSendCommand command) {
        var template = flow.template();
        for (var entry : command.model().entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return template;
    }
}
