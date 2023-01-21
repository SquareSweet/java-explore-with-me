package ru.practicum.explorewithme.stats.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
