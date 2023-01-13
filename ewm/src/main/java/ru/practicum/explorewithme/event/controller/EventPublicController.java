package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.dto.EventFullDto;
import ru.practicum.explorewithme.event.model.dto.EventShortDto;
import ru.practicum.explorewithme.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean availableOnly,
            @RequestParam(required = false) String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return eventService.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, availableOnly,
                sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        return eventService.getByIdPublic(eventId, request);
    }
}
