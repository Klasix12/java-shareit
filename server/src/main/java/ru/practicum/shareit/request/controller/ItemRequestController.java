package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addRequest(@RequestHeader(userIdHeader) Long userId,
                                     @RequestBody ItemRequestDto request) {
        request.setUserId(userId);
        return service.save(request);
    }

    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader(userIdHeader) Long userId) {
        return service.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ItemRequestDto getRequest(@PathVariable Long id) {
        return service.getById(id);
    }
}
