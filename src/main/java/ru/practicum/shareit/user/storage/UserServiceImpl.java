package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        log.info("Добавление пользователя {}", user);
        return userRepository.addUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + userId));
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        log.info("Обновление пользователя с id {}", id);
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя {}", id);
        userRepository.deleteUser(id);
    }
}
