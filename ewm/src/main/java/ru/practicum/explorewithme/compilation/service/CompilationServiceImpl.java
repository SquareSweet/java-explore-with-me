package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventService eventService;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            //throws exception if some events do not exist
            events.addAll(eventService.getByIdListNotMapped(newCompilationDto.getEvents()));
        }
        Compilation compilation = repository.save(mapper.toCompilation(newCompilationDto, events));
        log.info("Create compilation id: {}", compilation.getId());
        return mapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteById(Long compilationId) {
        repository.deleteById(compilationId);
        log.info("Delete compilation id: {}", compilationId);
    }

    @Override
    public CompilationDto addEvent(Long compilationId, Long eventId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        Event event = eventService.getByIdNotMapped(eventId); //throws exception if event does not exist
        compilation.getEvents().add(event);
        compilation = repository.save(compilation);
        log.info("Event id: {} added to compilation id: {}", eventId, compilationId);
        return mapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto removeEvent(Long compilationId, Long eventId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        Event event = eventService.getByIdNotMapped(eventId); //throws exception if event does not exist
        compilation.getEvents().remove(event);
        compilation = repository.save(compilation);
        log.info("Event id: {} removed from compilation id: {}", eventId, compilationId);
        return mapper.toCompilationDto(compilation);
    }

    @Override
    public void pin(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        compilation.setPinned(true);
        repository.save(compilation);
        log.info("Event id: {} pinned", compilationId);
    }

    @Override
    public void unpin(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        compilation.setPinned(false);
        repository.save(compilation);
        log.info("Event id: {} unpinned", compilationId);
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
        return mapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getByPinned(Boolean pinned, int from, int size) {
       return repository.findAllByPinned(pinned, OffsetPageRequest.of(from, size)).stream()
               .map(mapper::toCompilationDto)
               .collect(Collectors.toList());
    }
}
