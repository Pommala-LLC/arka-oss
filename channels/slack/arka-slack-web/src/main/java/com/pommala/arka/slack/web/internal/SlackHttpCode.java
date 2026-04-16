package com.pommala.arka.slack.web.internal;
import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.web.AppCodeHttpMapper;
import com.pommala.arka.slack.support.code.SlackTransportCode;
import org.springframework.http.HttpStatus;

public class SlackHttpCode implements AppCodeHttpMapper {
    @Override public HttpStatus map(ApplicationCode code) {
        if (code instanceof SlackTransportCode tc) {
            return switch (tc) {
                case API_UNAVAILABLE    -> HttpStatus.SERVICE_UNAVAILABLE;
                case API_TIMEOUT        -> HttpStatus.GATEWAY_TIMEOUT;
                case RATE_LIMITED       -> HttpStatus.TOO_MANY_REQUESTS;
                default                 -> HttpStatus.BAD_GATEWAY;
            };
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
