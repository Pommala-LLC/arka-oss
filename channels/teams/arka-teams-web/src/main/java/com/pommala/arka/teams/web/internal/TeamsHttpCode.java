package com.pommala.arka.teams.web.internal;
import com.pommala.arka.support.code.ApplicationCode;
import com.pommala.arka.web.AppCodeHttpMapper;
import com.pommala.arka.teams.support.code.TeamsTransportCode;
import org.springframework.http.HttpStatus;

public class TeamsHttpCode implements AppCodeHttpMapper {
    @Override public HttpStatus map(ApplicationCode code) {
        if (code instanceof TeamsTransportCode tc) {
            return switch (tc) {
                case GRAPH_UNAVAILABLE  -> HttpStatus.SERVICE_UNAVAILABLE;
                case GRAPH_TIMEOUT      -> HttpStatus.GATEWAY_TIMEOUT;
                case GRAPH_THROTTLED    -> HttpStatus.TOO_MANY_REQUESTS;
                default                 -> HttpStatus.BAD_GATEWAY;
            };
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
