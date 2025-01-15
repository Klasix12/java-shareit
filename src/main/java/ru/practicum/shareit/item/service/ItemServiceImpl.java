package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId, ItemDto item) {
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        log.info("Пользователь {} добавил предмет {}", user, item);
        return ItemMapper.toDto(itemRepository.save(ItemMapper.toEntity(item, user)));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto newItem, Long itemId) {
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        Item oldItem = ItemMapper.toEntity(getItemByIdOrThrow(itemId), user);
        if (newItem.getName() != null) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        log.info("Пользователь {} обновил предмет {}", user, oldItem);
        return ItemMapper.toDto(itemRepository.save(oldItem));
    }

    @Override
    public ItemDto getItemByIdOrThrow(Long itemId) {
        log.info("Получение предмета с id {}", itemId);
        return ItemMapper.toDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найден предмет", "Не найден предмет с id " + itemId)));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        log.info("Получение предметов пользователя с id {}", userId);
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        return itemRepository.findAllByOwner(user).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.info("Передана пустая строка");
            return Collections.emptyList();
        }
        log.info("Поиск предметов по слову {}", text);
        return itemRepository.findAllByNameOrDescription(text).stream()
                .map(ItemMapper::toDto)
                .toList();
    }
}
