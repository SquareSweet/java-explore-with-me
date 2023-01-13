package ru.practicum.explorewithme.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewCompilationDto {
    Boolean pinned;
    @NotBlank(message = "Compilation title should not be blank")
    String title;
    List<Long> events;
}
