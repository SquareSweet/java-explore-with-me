package ru.practicum.explorewithme.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class UserDto {
    Long id;
    @NotBlank(message = "User name should not be blank")
    String name;
    @NotBlank(message = "User email should not be blank")
    @Email(message = "User email should meet email format requirements")
    String email;
}
