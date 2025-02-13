package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto save(ItemRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + request.getUserId()));
        request.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toDto(repository.save(ItemRequestMapper.toEntity(request, user)));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        return ItemRequestMapper.toDto(repository.findAllByRequesterId(userId));
    }

    @Override
    public List<ItemRequestDto> getAll() {
        return ItemRequestMapper.toDto(repository.findAll());
    }

    @Override
    public ItemRequestDto getById(Long requestId) {
        return ItemRequestMapper.toDto(repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос", "Не найден запрос с id " + requestId)));
    }
}
