package ru.practicum.explorewithme.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto create(Long eventId, Long userId);

    List<RequestDto> getUserRequests(Long userId);

    RequestDto cancel(Long requestId, Long userId);

    List<RequestDto> getEventRequests(Long eventId, Long userId);

    @Transactional
    RequestDto confirm(Long requestId, Long eventId, Long userId);

    RequestDto reject(Long requestId, Long eventId, Long userId);
}
