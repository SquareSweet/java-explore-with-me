package ru.practicum.explorewithme.event.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Embeddable
public class Location {
    Float lat;
    Float lon;
}
