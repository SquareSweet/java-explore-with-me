package ru.practicum.explorewithme.mdoel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
