package ru.practicum.explorewithme.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.model.dto.CategoryDto;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}
