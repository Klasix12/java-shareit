package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
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
    public UserDto updateUser(Long id, UserDto user) {
        User oldUser = UserMapper.toEntity(getUserByIdOrThrow(id));
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.info("Обновление пользователя с id {}", id);
        return UserMapper.toDto(userRepository.save(oldUser));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя {}", id);
        getUserByIdOrThrow(id);
        userRepository.deleteById(id);
    }
}
