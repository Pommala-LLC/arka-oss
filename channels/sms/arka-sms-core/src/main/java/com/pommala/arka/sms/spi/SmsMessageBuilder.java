package com.pommala.arka.sms.spi;

import com.pommala.arka.sms.api.SmsSendCommand;
import com.pommala.arka.sms.model.FinalSmsMessage;
import com.pommala.arka.sms.model.ResolvedSmsFlow;

/**
 * SPI for constructing the send-ready SMS message.
 */
public interface SmsMessageBuilder {

    FinalSmsMessage build(ResolvedSmsFlow flow, SmsSendCommand command);
}
