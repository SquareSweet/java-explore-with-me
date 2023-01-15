package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(Long categoryId);

    CategoryDto getById(Long categoryId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
