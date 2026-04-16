package com.pommala.arka.sms.spi;

import com.pommala.arka.sms.model.FinalSmsMessage;
import com.pommala.arka.sms.support.exception.SmsTransportException;

/**
 * SPI for physical SMS delivery.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 */
public interface SmsTransport {

    void send(FinalSmsMessage message);
}
