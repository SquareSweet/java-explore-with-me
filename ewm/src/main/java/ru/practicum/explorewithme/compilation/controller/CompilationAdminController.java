package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.model.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createEvent(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.create(newCompilationDto);
    }

    @DeleteMapping("/{compilationId}")
    public void deleteUser(@PathVariable Long compilationId) {
        compilationService.deleteById(compilationId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable Long compilationId, @PathVariable Long eventId) {
        return compilationService.addEvent(compilationId, eventId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public CompilationDto removeEvent(@PathVariable Long compilationId, @PathVariable Long eventId) {
        return compilationService.removeEvent(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/pin")
    public void pinCompilation(@PathVariable Long compilationId) {
        compilationService.pin(compilationId);
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable Long compilationId) {
        compilationService.unpin(compilationId);
    }
}
