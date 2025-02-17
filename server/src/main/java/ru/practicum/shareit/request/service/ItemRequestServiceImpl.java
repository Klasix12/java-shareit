package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto save(ItemRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + request.getUserId()));
        request.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toDto(repository.save(ItemRequestMapper.toEntity(request, user)));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        List<ItemRequest> requests = repository.findAllByRequesterId(userId);
        List<Item> requestsItems = itemRepository.findAllByItemRequestIds(
                requests.stream()
                        .map(ItemRequest::getId)
                        .collect(Collectors.toList()));
        return createRequestsWithItems(requests, requestsItems);
    }

    @Override
    public List<ItemRequestDto> getAll() {
        return ItemRequestMapper.toDto(repository.findAll());
    }

    @Override
    public ItemRequestDto getById(Long requestId) {
        ItemRequestDto request = ItemRequestMapper.toDto(repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос", "Не найден запрос с id " + requestId)));
        List<Item> items = itemRepository.findAllByItemRequestId(requestId);
        System.out.println(items);
        request.setItems(ItemMapper.toDto(items));
        return request;
    }

    private List<ItemRequestDto> createRequestsWithItems(List<ItemRequest> requests, List<Item> items) {
        Map<Long, ItemRequestDto> itemRequestDtos = requests.stream()
                .map(ItemRequestMapper::toDto)
                .peek(i -> i.setItems(Collections.emptyList()))
                .collect(Collectors.toMap(ItemRequestDto::getId, i -> i));

        items.forEach(item -> itemRequestDtos.get(item.getItemRequest().getId())
                .getItems().add(ItemMapper.toDto(item)));

        return new ArrayList<>(itemRequestDtos.values());
    }
}
