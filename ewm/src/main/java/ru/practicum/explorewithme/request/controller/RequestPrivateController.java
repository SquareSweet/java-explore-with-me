package ru.practicum.explorewithme.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.model.dto.RequestDto;
import ru.practicum.explorewithme.request.service.RequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequestByUserId(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    public RequestDto createRequest(@RequestParam Long eventId, @PathVariable Long userId) {
        return requestService.create(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long requestId, @PathVariable Long userId) {
        return requestService.cancel(requestId, userId);
    }
}
