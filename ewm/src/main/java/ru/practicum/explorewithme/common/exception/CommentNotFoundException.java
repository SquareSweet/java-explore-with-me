package ru.practicum.explorewithme.common.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Comment does not exist: " + id);
    }
}
