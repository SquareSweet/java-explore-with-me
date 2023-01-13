package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return compilationService.getByPinned(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilations(@PathVariable Long compilationId) {
        return compilationService.getById(compilationId);
    }
}
