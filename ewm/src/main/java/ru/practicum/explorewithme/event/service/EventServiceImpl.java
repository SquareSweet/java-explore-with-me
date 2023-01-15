package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.EventNotFoundException;
import ru.practicum.explorewithme.common.exception.IllegalOperationException;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequestDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryService categoryService;
    private final UserServiceImpl userService;
    private final EventMapper mapper;

    @Override
    public EventFullDto create(NewEventDto newEventDto, Long userId) {
        User user = userService.getById(userId); //throws exception if user does not exist
        CategoryDto category = categoryService.getById(newEventDto.getCategory()); //throws exception if category does not exist
        Event event = mapper.toEvent(newEventDto, category, user);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event = repository.save(event);
        log.info("Created event id: {}", event.getId());
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto update(NewEventDto newEventDto, Long eventId) {
        CategoryDto category = categoryService.getById(newEventDto.getCategory()); //throws exception if category does not exist
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Event updatedEvent = mapper.toEvent(newEventDto, category, event.getInitiator());
        event = updateEventFields(event, updatedEvent);
        repository.save(event);
        log.info("Updated event id: {}", eventId);
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto publish(Long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != EventStatus.PENDING) {
            throw new IllegalOperationException("Event id: " + eventId + " does not await moderation");
        }
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("Событие {} начинается менее чем через час", event.getId());
            throw new IllegalOperationException("Event id: " + eventId + " starts in less than an hour");
        }
        event.setState(EventStatus.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        repository.save(event);
        log.info("Published event id: {}", eventId);
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto reject(Long eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != EventStatus.PENDING) {
            throw new IllegalOperationException("Event id: " + eventId + " does not await moderation");
        }
        event.setState(EventStatus.CANCELED);
        repository.save(event);
        log.info("Rejected event id: {}", eventId);
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateByUser(UpdateEventRequestDto updateEventRequestDto, Long userId) {
        userService.getById(userId); //throws exception if user does not exist
        CategoryDto category = null;
        if (updateEventRequestDto.getCategory() != null) {
            category = categoryService.getById(updateEventRequestDto.getCategory()); //throws exception if category does not exist
        }
        Event event = repository.findById(updateEventRequestDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException(updateEventRequestDto.getEventId()));
        checkInitiator(event, userId);
        Event updatedEvent = mapper.toEvent(updateEventRequestDto, category);
        event = updateEventFields(event, updatedEvent);
        repository.save(event);
        log.info("Updated event id: {}", event.getId());
        return mapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancel(Long eventId, Long userId) {
        userService.getById(userId); //throws exception if user does not exist
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != EventStatus.PENDING) {
            throw new IllegalOperationException("Event id: " + eventId + " does not await moderation");
        }
        checkInitiator(event, userId);
        event.setState(EventStatus.CANCELED);
        repository.save(event);
        log.info("Canceled event id: {}", eventId);
        return mapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> searchByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        List<EventStatus> eventStatusList;
        if (states == null) {
            eventStatusList = List.of(EventStatus.values());
        } else {
            eventStatusList = states.stream().map(EventStatus::valueOf).collect(Collectors.toList());
        }
        return repository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(
                users, eventStatusList, categories, rangeStart, rangeEnd, OffsetPageRequest.of(from, size)
        ).stream().map(mapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getByUserId(Long userId, int from, int size) {
        userService.getById(userId); //throws exception if user does not exist
        return repository.findAllByInitiatorId(userId, OffsetPageRequest.of(from, size)).stream()
                .map(mapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getById(Long eventId, Long userId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        checkInitiator(event, userId);
        return mapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto getByIdPublic(Long eventId) {
        Event event = repository.findAllByIdAndState(eventId, EventStatus.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setViews(event.getViews() + 1);
        repository.save(event);
        return mapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, String sort, int from, int size) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        Sort sortValue;
        List<Event> events;
        if (sort == null) {
            sortValue = Sort.unsorted();
        } else if (sort.equalsIgnoreCase("views")) {
            sortValue = Sort.by("views").ascending();
        } else if (sort.equalsIgnoreCase("event_date")) {
            sortValue = Sort.by("eventDate").ascending();
        } else {
            throw new IllegalArgumentException("Unknown sort parameter: " + sort);
        }
        if (onlyAvailable) {
            events = repository.findAllByParamsAvailable(text, categories, paid, rangeStart, rangeEnd,
                    EventStatus.PUBLISHED, OffsetPageRequest.of(from, size, sortValue));
        } else {
            events = repository.findAllByParams(text, categories, paid, rangeStart, rangeEnd, EventStatus.PUBLISHED,
                    OffsetPageRequest.of(from, size, sortValue));
        }
        return events.stream().map(mapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public Event getByIdNotMapped(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public Event addConfirmedRequest(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        return repository.save(event);
    }

    private void checkInitiator(Event event, Long userId) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not initiator of event id: " + event.getId());
        }
    }

    private Event updateEventFields(Event event, Event updatedEvent) {
        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            event.setCategory(updatedEvent.getCategory());
        }
        if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().isBlank()) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getEventDate() != null) {
            event.setEventDate(updatedEvent.getEventDate());
        }
        if (updatedEvent.getLocation() != null) {
            event.setLocation(updatedEvent.getLocation());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().isBlank()) {
            event.setTitle(updatedEvent.getTitle());
        }
        return event;
    }
}
