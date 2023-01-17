package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.common.exception.IllegalOperationException;
import ru.practicum.explorewithme.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = repository.save(mapper.toCategory(newCategoryDto));
        log.info("Created category id: {}", category.getId());
        return mapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = repository.findById(categoryDto.getId())
                .orElseThrow(() -> new CategoryNotFoundException(categoryDto.getId()));
        category.setName(categoryDto.getName());
        category = repository.save(category);
        log.info("Updated category id: {}", category.getId());
        return mapper.toCategoryDto(category);
    }

    @Override
    public void deleteById(Long categoryId) {
        if (eventRepository.countAllByCategoryId(categoryId) > 0) {
            throw new IllegalOperationException("Category id: " + categoryId + " can not be deleted while has events");
        }
        repository.deleteById(categoryId);
        log.info("Deleted category id: {}", categoryId);
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        return mapper.toCategoryDto(repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId)));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return repository.findAll(OffsetPageRequest.of(from, size)).stream()
                .map(mapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
