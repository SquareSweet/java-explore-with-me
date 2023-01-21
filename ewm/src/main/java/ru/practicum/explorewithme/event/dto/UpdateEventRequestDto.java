package ru.practicum.explorewithme.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class UpdateEventRequestDto {
    @NotNull(message = "Event id should be specified")
    Long eventId;
    String annotation;
    Long category;
    String description;
    String eventDate;
    Integer participantLimit;
    Boolean requestModeration;
    Boolean paid;
    String title;
}
