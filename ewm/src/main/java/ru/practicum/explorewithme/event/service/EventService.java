package ru.practicum.explorewithme.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(NewEventDto newEventDto, Long userId);

    EventFullDto update(UpdateEventAdminDto updateEventAdminDto, Long eventId);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);

    EventFullDto updateByUser(UpdateEventRequestDto updateEventRequestDto, Long userId);

    EventFullDto cancel(Long eventId, Long userId);

    List<EventFullDto> searchByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventFullDto> getByUserId(Long userId, int from, int size);

    EventFullDto getById(Long eventId, Long userId);

    @Transactional
    EventFullDto getByIdPublic(Long eventId);

    List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Boolean onlyAvailable, String sort, int from, int size);

    Event getByIdNotMapped(Long eventId);

    List<Event> getByIdListNotMapped(List<Long> eventIds);

    Event addConfirmedRequest(Event event);
}
