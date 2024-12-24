package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto getUserByIdOrThrow(Long userId);

    UserDto updateUser(Long id, UserDto user);

    void deleteUser(Long id);
}
