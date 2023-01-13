package ru.practicum.explorewithme.request.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.request.model.RequestStatus;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class RequestDto {
    Long id;
    LocalDateTime created;
    Long event;
    Long requester;
    RequestStatus status;
}
