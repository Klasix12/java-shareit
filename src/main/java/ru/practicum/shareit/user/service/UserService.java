package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto getUserByIdOrThrow(Long userId);

    UserDto updateUser(Long id, UserDto user);

    void deleteUser(Long id);

    Optional<User> getUserById(Long id);

    Boolean isUserExists(Long id);
}
