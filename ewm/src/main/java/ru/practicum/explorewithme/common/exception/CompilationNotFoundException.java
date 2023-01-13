package ru.practicum.explorewithme.common.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(Long id) {
        super("Compilation does not exist: " + id);
    }
}
