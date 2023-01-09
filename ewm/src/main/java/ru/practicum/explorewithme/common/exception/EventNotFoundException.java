package ru.practicum.explorewithme.common.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event does not exist: " + id);
    }
}
