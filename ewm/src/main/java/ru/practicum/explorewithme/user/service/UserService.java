package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.OffsetPageRequest;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.dto.UserDto;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto create(UserDto userDto) {
        User user = repository.save(mapper.toUser(userDto));
        log.info("Create user id: {}", user.getId());
        return mapper.toUserDto(user);
    }

    public void deleteById(Long userId) {
        repository.deleteById(userId);
        log.info("Delete user id: {}", userId);
    }

    public List<UserDto> getAll(Long[] ids, int from, int size) {
        return repository.findAllByIdIn(List.of(ids), OffsetPageRequest.of(from, size)).stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }
}
