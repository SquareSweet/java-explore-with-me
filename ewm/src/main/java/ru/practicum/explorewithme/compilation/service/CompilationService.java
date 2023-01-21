package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Long compilationId);

    CompilationDto addEvent(Long compilationId, Long eventId);

    CompilationDto removeEvent(Long compilationId, Long eventId);

    void pin(Long compilationId);

    void unpin(Long compilationId);

    CompilationDto getById(Long compilationId);

    List<CompilationDto> getByPinned(Boolean pinned, int from, int size);
}
