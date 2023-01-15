package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    void deleteById(Long userId);

    List<UserDto> getAll(List<Long> ids, int from, int size);

    User getById(Long userId);
}
