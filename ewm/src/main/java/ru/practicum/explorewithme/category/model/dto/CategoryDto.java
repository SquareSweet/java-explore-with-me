package ru.practicum.explorewithme.category.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CategoryDto {
    Long id;
    @NotBlank(message = "Category name should not be blank")
    String name;
}
