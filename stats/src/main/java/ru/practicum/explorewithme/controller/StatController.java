package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mdoel.EndpointHit;
import ru.practicum.explorewithme.mdoel.ViewStats;
import ru.practicum.explorewithme.service.StatService;

import java.util.List;

@RestController
@Slf4j
public class StatController {
    private final StatService service;

    @Autowired
    public StatController(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.OK)
    public EndpointHit hit(@RequestBody EndpointHit endpointHit) {
        log.info("Got POST request to /hit; endpointHit = " + endpointHit);
        return service.save(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStat(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info(String.format("Got GET request to /stats; start = %s, end = %s, uris = %s, unique = %s",
                start, end, uris, unique));
        return service.getStats(start, end, uris, unique);
    }
}
