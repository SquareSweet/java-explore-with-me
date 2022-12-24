package ru.practicum.explorewithme.stats.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class EndpointHit {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
