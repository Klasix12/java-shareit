package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto user) {
        log.trace("Добавление пользователя");
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto user,
                              @PathVariable Long id) {
        log.trace("Обновление пользователя");
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.trace("Получение пользователя");
        return userService.getUserByIdOrThrow(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.trace("Удаление пользователя");
        userService.deleteUser(id);
    }
}
