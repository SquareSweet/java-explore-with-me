package ru.practicum.explorewithme.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}
