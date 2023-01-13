package ru.practicum.explorewithme.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.dto.UserDto;
import ru.practicum.explorewithme.user.model.dto.UserShortDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}
