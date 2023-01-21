package ru.practicum.explorewithme.common.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long id) {
        super("Participation request does not exist: " + id);
    }
}
