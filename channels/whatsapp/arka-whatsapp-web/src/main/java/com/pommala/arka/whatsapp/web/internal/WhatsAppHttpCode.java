package com.pommala.arka.whatsapp.web.internal;

import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.web.AppCodeHttpMapper;
import com.pommala.arka.whatsapp.support.code.WhatsAppTransportCode;
import org.springframework.http.HttpStatus;

public class WhatsAppHttpCode implements AppCodeHttpMapper {
    @Override
    public HttpStatus map(ApplicationCode code) {
        if (code instanceof WhatsAppTransportCode tc) {
            return switch (tc) {
                case API_UNAVAILABLE       -> HttpStatus.SERVICE_UNAVAILABLE;
                case API_TIMEOUT           -> HttpStatus.GATEWAY_TIMEOUT;
                case API_RATE_LIMITED      -> HttpStatus.TOO_MANY_REQUESTS;
                default                    -> HttpStatus.BAD_GATEWAY;
            };
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
