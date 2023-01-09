package ru.practicum.explorewithme.event.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Embeddable
public class Location {
    float lat;
    float lon;
}
