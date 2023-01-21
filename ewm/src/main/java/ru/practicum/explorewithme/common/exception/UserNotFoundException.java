package ru.practicum.explorewithme.common.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User does not exist: " + id);
    }
}
