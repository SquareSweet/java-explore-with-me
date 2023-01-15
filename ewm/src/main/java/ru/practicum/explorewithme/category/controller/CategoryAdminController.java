package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}
