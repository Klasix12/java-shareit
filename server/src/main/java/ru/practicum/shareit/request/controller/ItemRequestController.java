package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addRequest(@RequestHeader(userIdHeader) Long userId,
                                     @RequestBody ItemRequestDto request) {
        log.info("Добавление нового запроса {}, userId={}", request, userId);
        request.setUserId(userId);
        return service.save(request);
    }

    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader(userIdHeader) Long userId) {
        log.info("Получение запросов пользователя {}", userId);
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestParam(name = "from") Integer from,
                                                     @RequestParam(name = "size") Integer size) {
        log.info("Получение всех запросов, from={}, size={}", from, size);
        return service.getAll(from, size);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequest(@PathVariable Long id) {
        log.info("Получение запроса {}", id);
        return service.getById(id);
    }
}
