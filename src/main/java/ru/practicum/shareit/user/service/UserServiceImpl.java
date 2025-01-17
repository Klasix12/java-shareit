package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto user) {
        log.info("Добавление пользователя {}", user);
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(user)));
    }

    @Override
    public UserDto getUserByIdOrThrow(Long userId) {
        log.info("Получение пользователя с id {}", userId);
        return UserMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + userId)));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto user) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + id));
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.info("Обновление пользователя с id {}", id);
        return UserMapper.toDto(oldUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя {}", id);
        userRepository.deleteById(id);
    }
}
