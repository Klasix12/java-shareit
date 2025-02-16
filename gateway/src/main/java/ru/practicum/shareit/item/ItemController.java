package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping({"", "/"})
    public ResponseEntity<Object> addItem(@RequestHeader(userIdHeader) @NotNull Long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(userIdHeader) @NotNull Long userId,
                                             @RequestBody ItemDto item,
                                             @PathVariable Long itemId) {
        log.info("Updating item {}, userId={}, itemId={}", item, userId, itemId);
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId) {
        log.info("Get item {}", itemId);
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(userIdHeader) @NotNull Long userId) {
        log.info("Get user items, userId={}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text) {
        log.info("Search items, text={}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(userIdHeader) @NotNull Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentDto comment) {
        log.info("Creating comment {}, userId={}, itemId={}", comment, userId, itemId);
        return itemClient.addComment(userId, itemId, comment);
    }
}
