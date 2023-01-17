package ru.practicum.explorewithme.common.exception;

import java.util.Set;
import java.util.stream.Collectors;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event does not exist: " + id);
    }

    public EventNotFoundException(Set<Long> ids) {
        super("Events do not exist: " +  ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
}
