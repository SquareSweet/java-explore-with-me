package ru.practicum.explorewithme.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.service.RequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/requests")
public class EventRequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequestByEventId(@PathVariable Long eventId,
                                                @PathVariable Long userId) {
        return requestService.getEventRequests(eventId, userId);
    }

    @PatchMapping("/{requestId}/confirm")
    public RequestDto confirmRequest(@PathVariable Long requestId,
                                     @PathVariable Long eventId,
                                     @PathVariable Long userId) {
        return requestService.confirm(requestId, eventId, userId);
    }

    @PatchMapping("/{requestId}/reject")
    public RequestDto rejectRequest(@PathVariable Long requestId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long userId) {
        return requestService.reject(requestId, eventId, userId);
    }
}
