package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validators.OnCreate;
import ru.practicum.shareit.validators.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final String userIdHeader = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(userIdHeader) @NotNull Long userId,
                           @Validated(OnCreate.class) @RequestBody ItemDto item) {
        log.trace("Добавление предмета");
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(userIdHeader) @NotNull Long userId,
                              @Validated(OnUpdate.class) @RequestBody ItemDto item,
                              @PathVariable Long itemId) {
        log.trace("Обновление предмета");
        return itemService.updateItem(userId, item, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        log.trace("Получение предмета");
        return itemService.getItemAndCommentsOrThrow(itemId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(userIdHeader) @NotNull Long userId) {
        log.trace("Получение предметов пользователя");
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.trace("Поиск предметов");
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(userIdHeader) @NotNull Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.trace("Добавление комментария");
        return itemService.addComment(userId, itemId, commentRequestDto);
    }
}
