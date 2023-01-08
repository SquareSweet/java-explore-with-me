package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.dto.CategoryDto;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryDto create(CategoryDto categoryDto) {
        Category category = repository.save(mapper.toCategory(categoryDto));
        log.info("Created category id: {}", category.getId());
        return mapper.toCategoryDto(category);
    }

    public CategoryDto update(CategoryDto categoryDto) {
        Category category = repository.findById(categoryDto.getId())
                .orElseThrow(() -> new CategoryNotFoundException(categoryDto.getId()));
        category.setName(categoryDto.getName());
        category = repository.save(category);
        log.info("Updated category id: {}", category.getId());
        return mapper.toCategoryDto(category);
    }

    public void deleteById(Long categoryId) {
        //TODO: проверка наушения целостности данных после реализации ивентов
        repository.deleteById(categoryId);
        log.info("Deleted category id: {}", categoryId);
    }

    public CategoryDto getById(Long categoryId) {
        return mapper.toCategoryDto(repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId)));
    }

    public List<CategoryDto> getAll(Integer from, Integer size) {
        return repository.findAll(OffsetPageRequest.of(from, size)).stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
