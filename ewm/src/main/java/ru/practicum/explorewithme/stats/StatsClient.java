package ru.practicum.explorewithme.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.stats.model.EndpointHit;
import ru.practicum.explorewithme.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsClient {
    private final RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void saveHit(EndpointHit endpointHit) {
        post("/hit", endpointHit);
    }

    public Long getViews(Long eventId) {
        String uris = "/events/" + eventId;
        Map<String, Object> parameters = getParamsForUris(uris);
        try {
            ResponseEntity<ViewStats[]> response = rest.getForEntity(
                    "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    ViewStats[].class,
                    parameters
            );
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody() && response.getBody().length > 0) {
                return response.getBody()[0].getHits();
            } else {
                return 0L;
            }
        } catch (HttpStatusCodeException e) {
            log.debug(e.getMessage());
            return 0L;
        }
    }

    public Map<Long, Long> getViewsMap(List<Long> eventIds) {
        String uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.joining(","));
        Map<String, Object> parameters = getParamsForUris(uris);
        try {
            ResponseEntity<ViewStats[]> response = rest.getForEntity("/stats", ViewStats[].class, parameters);
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                Map<Long, Long> viewsMap = new HashMap<>();
                for (ViewStats stats : response.getBody()) {
                    viewsMap.put(Long.parseLong(stats.getUri().split("/")[2]), stats.getHits());
                }
                return viewsMap;
            } else {
                return Map.of();
            }
        } catch (HttpStatusCodeException e) {
            log.debug(e.getMessage());
            return Map.of();
        }
    }

    private Map<String, Object> getParamsForUris(String uris) {
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        return Map.of(
                "start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "uris", uris,
                "unique", false
        );
    }

    private ResponseEntity<Object> post(String path, EndpointHit body) {
        HttpEntity<EndpointHit> requestEntity = new HttpEntity<>(body);

        ResponseEntity<Object> response;
        try {
            response = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(response);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
