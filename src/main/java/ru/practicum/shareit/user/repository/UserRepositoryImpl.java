package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailAlreadyRegistered;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Long userId = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (isEmailAlreadyRegistered(user)) {
            throw new EmailAlreadyRegistered("Неверный email", "Пользователь с таким email уже зарегистрирован");
        }
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + userId);
        }
        if (isEmailAlreadyRegistered(user)) {
            throw new EmailAlreadyRegistered("Неверный email", "Пользователь с таким email уже зарегистрирован");
        }
        User newUser = users.get(user.getId());
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        newUser.setId(user.getId());
        return newUser;
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    private boolean isEmailAlreadyRegistered(User newUser) {
        for (User user : users.values()) {
            if (!Objects.equals(newUser.getId(), user.getId()) && user.getEmail().equals(newUser.getEmail())) {
                return true;
            }
        }
        return false;
    }
}