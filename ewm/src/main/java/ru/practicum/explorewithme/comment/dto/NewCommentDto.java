package ru.practicum.explorewithme.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class NewCommentDto {
    @NotNull(message = "Event id should be specified")
    Long event;
    @NotBlank(message = "Comment text should not be blank")
    String text;
}
