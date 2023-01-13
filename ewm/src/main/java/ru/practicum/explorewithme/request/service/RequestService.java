package ru.practicum.explorewithme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.common.exception.IllegalOperationException;
import ru.practicum.explorewithme.common.exception.RequestNotFoundException;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.mapper.RequestMapper;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestStatus;
import ru.practicum.explorewithme.request.model.dto.RequestDto;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository repository;
    private final RequestMapper mapper;
    private final UserService userService;
    private final EventService eventService;

    public RequestDto create(Long eventId, Long userId) {
        User user = userService.getById(userId); //trows exception if user does not exist
        Event event = eventService.getByIdForRequest(eventId); //trows exception if user does not exist
        if (event.getInitiator().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is the initiator of event id: " + event.getId());
        }
        if (event.getParticipantLimit() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new IllegalOperationException("Event id: " + event.getId() + " reached maximum participants");
        }
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new IllegalOperationException("Event id: " + event.getId() + " is not published");
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();
        request = repository.save(request);
        log.info("Created request id: {}", request.getId());
        return mapper.toRequestDto(request);
    }

    public List<RequestDto> getUserRequests(Long userId) {
        userService.getById(userId); //trows exception is user does not exist
        return repository.findAllByRequesterId(userId).stream().map(mapper::toRequestDto).collect(Collectors.toList());
    }

    public RequestDto cancel(Long requestId, Long userId) {
        userService.getById(userId); //trows exception is user does not exist
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        if (!request.getRequester().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not the author of request id: " + requestId);
        }
        if (request.getStatus() == RequestStatus.CANCELED) {
            throw new IllegalOperationException("Request id: " + requestId + " is already canceled");
        }
        request.setStatus(RequestStatus.CANCELED);
        repository.save(request);
        log.info("Canceled request id: {}", requestId);
        return mapper.toRequestDto(request);
    }

    public List<RequestDto> getEventRequests(Long eventId, Long userId) {
        userService.getById(userId); //trows exception if user does not exist
        eventService.getByIdForRequest(eventId); //trows exception if user does not exist
        return repository.findAllByEventId(eventId).stream().map(mapper::toRequestDto).collect(Collectors.toList());
    }

    @Transactional
    public RequestDto confirm(Long requestId, Long eventId, Long userId) {
        userService.getById(userId); //trows exception is user does not exist
        Event event = eventService.getByIdForRequest(eventId); //trows exception if user does not exist
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not the initiator of event id: " + eventId);
        }
        if (event.getParticipantLimit() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new IllegalOperationException("Event id: " + event.getId() + " reached maximum participants");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        repository.save(request);
        event = eventService.addConfirmedRequest(event);
        log.info("Confirmed request id: {}", requestId);
        if (event.getParticipantLimit() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            repository.rejectAllPendingRequests(eventId);
            log.info("All pending requests for event id: " + eventId + " canceled due to reaching maximum participants");
        }
        return mapper.toRequestDto(request);
    }

    public RequestDto reject(Long requestId, Long eventId, Long userId) {
        userService.getById(userId); //trows exception is user does not exist
        Event event = eventService.getByIdForRequest(eventId); //trows exception if user does not exist
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalOperationException("User id: " + userId + " is not the initiator of event id: " + eventId);
        }
        request.setStatus(RequestStatus.REJECTED);
        repository.save(request);
        log.info("Rejected request id: {}", requestId);
        return mapper.toRequestDto(request);
    }
}
