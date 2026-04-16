package com.pommala.arka.client.internal;

import com.pommala.arka.client.ArkaClient;
import com.pommala.arka.client.properties.ArkaClientProperties;
import com.pommala.arka.support.code.ArkaClientCode;
import com.pommala.arka.support.exception.ArkaApplicationException;
import com.pommala.arka.support.exception.ArkaTransportException;
import com.pommala.arka.support.exception.ArkaTransportTimeoutException;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/**
 * Default Arka client backed by Spring 7 {@link RestClient}.
 *
 * <p>Calls the Arka REST API at {@code /api/v1/{channel}/send} and
 * {@code /api/v1/{channel}/batch}. Translates HTTP errors to typed
 * Arka exceptions.
 */
public class DefaultArkaClient implements ArkaClient {

    private static final String LOG_PREFIX = "[arka-client]";
    private static final Logger log = LoggerFactory.getLogger(DefaultArkaClient.class);
    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestClient restClient;

    public DefaultArkaClient(ArkaClientProperties props) {
        Objects.requireNonNull(props.getBaseUrl(), "arka.client.base-url must be set");

        var builder = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        if (props.getApiKey() != null && !props.getApiKey().isBlank()) {
            builder.defaultHeader("X-API-Key", props.getApiKey());
        }

        this.restClient = builder.build();
    }

    @Override
    public Map<String, Object> send(String channel, Map<String, Object> request) {
        Objects.requireNonNull(channel, "channel must not be null");
        Objects.requireNonNull(request, "request must not be null");

        var uri = "/api/v1/" + channel + "/send";
        log.info("{} Sending to {} [flowKey={}]", LOG_PREFIX, channel, request.get("flowKey"));

        return execute(uri, request);
    }

    @Override
    public Map<String, Object> batch(String channel, Map<String, Object> request) {
        Objects.requireNonNull(channel, "channel must not be null");
        Objects.requireNonNull(request, "request must not be null");

        var uri = "/api/v1/" + channel + "/batch";
        log.info("{} Batch sending to {} [flowKey={}]", LOG_PREFIX, channel, request.get("flowKey"));

        return execute(uri, request);
    }

    private Map<String, Object> execute(String uri, Map<String, Object> request) {
        try {
            var response = restClient.post()
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(MAP_TYPE);

            return response != null ? response : Map.of();

        } catch (ResourceAccessException ex) {
            if (ex.getCause() instanceof java.net.SocketTimeoutException) {
                throw new ArkaTransportTimeoutException(ArkaClientCode.CLIENT_TIMEOUT,
                        "Arka API request timed out: " + uri, ex);
            }
            throw new ArkaTransportException(ArkaClientCode.CLIENT_CONNECTION_FAILED,
                    "Arka API connection failed: " + uri, ex);
        } catch (Exception ex) {
            throw new ArkaTransportException(ArkaClientCode.CLIENT_REQUEST_FAILED,
                    "Arka API request failed: " + uri + " — " + ex.getMessage(), ex);
        }
    }
}
