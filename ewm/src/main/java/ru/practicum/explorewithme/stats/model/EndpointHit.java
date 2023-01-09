package ru.practicum.explorewithme.stats.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class EndpointHit {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
