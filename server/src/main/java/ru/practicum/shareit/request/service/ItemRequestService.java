package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto save(ItemRequestDto request);

    List<ItemRequestDto> getUserRequests(Long userId);

    List<ItemRequestDto> getAll();

    ItemRequestDto getById(Long requestId);
}
