package ru.practicum.explorewithme.common.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category does not exist: " + id);
    }
}
