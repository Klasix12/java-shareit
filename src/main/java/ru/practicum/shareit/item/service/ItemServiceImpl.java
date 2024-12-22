package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId, Item item) {
        User user = userService.getUserById(userId);
        item.setOwner(user);
        log.info("Пользователь {} добавил предмет {}", user, item);
        return ItemMapper.toDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Item item, Long itemId) {
        User user = userService.getUserById(userId);
        item.setOwner(user);
        item.setId(itemId);
        log.info("Пользователь {} обновил предмет {}", user, item);
        return ItemMapper.toDto(itemRepository.updateItem(item));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        log.info("Получение предмета с id {}", itemId);
        return ItemMapper.toDto(itemRepository.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Не найден предмет", "Не найден предмет с id " + itemId)));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        log.info("Получение предметов пользователя с id {}", userId);
        return itemRepository.getItems(userId).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        log.info("Поиск предметов по слову {}", text);
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
