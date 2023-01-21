package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.common.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User user = repository.save(mapper.toUser(userDto));
        log.info("Create user id: {}", user.getId());
        return mapper.toUserDto(user);
    }

    @Override
    public void deleteById(Long userId) {
        repository.deleteById(userId);
        log.info("Delete user id: {}", userId);
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids == null) {
            users = repository.findAll(OffsetPageRequest.of(from, size)).toList();
        } else {
            users = repository.findAllByIdIn(ids, OffsetPageRequest.of(from, size));
        }
        return users.stream().map(mapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public User getById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
