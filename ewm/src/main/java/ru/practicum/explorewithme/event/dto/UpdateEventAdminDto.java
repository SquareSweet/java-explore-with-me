package ru.practicum.explorewithme.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.event.model.Location;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class UpdateEventAdminDto {
    String annotation;
    Long category;
    String description;
    String eventDate;
    Location location;
    Integer participantLimit;
    Boolean requestModeration;
    Boolean paid;
    String title;
}
