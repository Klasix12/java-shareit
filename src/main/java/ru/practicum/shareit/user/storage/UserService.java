package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    User addUser(User user);

    User getUserById(Long userId);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
