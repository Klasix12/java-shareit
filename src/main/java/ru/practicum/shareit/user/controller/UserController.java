package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserService;
import ru.practicum.shareit.validators.OnCreate;
import ru.practicum.shareit.validators.OnUpdate;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated(OnCreate.class) @RequestBody User user) {
        log.trace("Добавление пользователя");
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@Validated(OnUpdate.class) @RequestBody User user,
                           @PathVariable Long id) {
        log.trace("Обновление пользователя");
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.trace("Получение пользователя");
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.trace("Удаление пользователя");
        userService.deleteUser(id);
    }
}
