package ru.practicum.explorewithme.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.model.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.model.dto.NewCompilationDto;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventService eventService;
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(eventService.getByIdList(newCompilationDto.getEvents()))
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
