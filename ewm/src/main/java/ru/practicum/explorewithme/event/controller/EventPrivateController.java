package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequestDto;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByUserId(@PathVariable Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        return eventService.getByUserId(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateEventByUser(@Valid @RequestBody UpdateEventRequestDto updateEventRequestDto,
                                          @PathVariable Long userId) {
        return eventService.updateByUser(updateEventRequestDto, userId);
    }

    @PostMapping
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
        return eventService.create(newEventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventService.getById(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventService.cancel(eventId, userId);
    }
}
