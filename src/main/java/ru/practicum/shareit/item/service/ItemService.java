package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, Item item);

    ItemDto updateItem(Long userId, Item item, Long itemId);

    ItemDto getItem(Long itemId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);
}
