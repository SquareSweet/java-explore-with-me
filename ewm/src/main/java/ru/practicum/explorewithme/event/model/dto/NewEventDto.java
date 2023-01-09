package ru.practicum.explorewithme.event.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewEventDto {
    @NotBlank(message = "Event annotation should not be blank")
    String annotation;
    @NotNull(message = "Event category should be specified")
    Long category;
    @NotBlank(message = "Event description should not be blank")
    String description;
    @NotBlank(message = "Event date should be specified")
    String eventDate;
    @NotNull(message = "Event location should be specified")
    Location location;
    Integer participantLimit;
    Boolean requestModeration;
    Boolean paid;
    @NotBlank(message = "Event title should not be blank")
    String title;
}
