package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserNotItemOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemDto item) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + userId));
        ItemRequest req = null;
        if (item.hasRequestId()) {
            req = itemRequestRepository.findById(item.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Не найден запрос", "Не найден запрос с id " + item.getRequestId()));
        }
        log.info("Пользователь {} добавил предмет {}", userId, item);
        return ItemMapper.toDto(itemRepository.save(ItemMapper.toEntity(item, user, req)));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, ItemDto newItem, Long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найден предмет", "Не найден предмет с id " + itemId));
        if (!Objects.equals(userId, oldItem.getOwner().getId())) {
            log.warn("Пользователь {} попытался обновить вещь {} не являясь владельцем", userId, itemId);
            throw new UserNotItemOwnerException("Ошибка пользователя",
                    "Пользователь " + userId + " не является владельцем вещи " + itemId);
        }
        if (newItem.getName() != null) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        log.info("Пользователь {} обновил предмет {}", userId, oldItem);
        return ItemMapper.toDto(oldItem);
    }

    @Override
    public ItemDto getItemAndCommentsOrThrow(Long itemId) {
        log.info("Получение предмета с id {}", itemId);
        ItemDto itemDto = ItemMapper.toDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найден предмет", "Не найден предмет с id " + itemId)));
        itemDto.setComments(commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList()));
        return itemDto;
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        log.info("Получение предметов пользователя с id {}", userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        List<Comment> comments = commentRepository.findAllByItemsIds(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return createItemDtosWithComments(comments, items);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty() || text.isBlank()) {
            log.info("Передана пустая строка");
            return Collections.emptyList();
        }
        log.info("Поиск предметов по слову {}", text);
        return ItemMapper.toDto(itemRepository.findAllByNameOrDescription(text));
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        log.info("Пользователь " + userId + " оставляет комментарий: \"" + commentRequestDto.getText() + "\" предмету " + itemId);
        Booking booking = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndEndBefore(
                        itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new CommentException("Ошибка пользователя",
                        "Пользователь " + userId + " не бронировал предмет " + itemId));
        commentRequestDto.setCreated(LocalDateTime.now());
        return CommentMapper.toDto(commentRepository
                .save(CommentMapper.toEntity(commentRequestDto, booking.getBooker(), booking.getItem())));
    }

    private List<ItemDto> createItemDtosWithComments(List<Comment> comments, List<Item> items) {
        Map<Long, ItemDto> itemDtos = items.stream()
                .map(ItemMapper::toDto)
                .peek(i -> i.setComments(Collections.emptyList()))
                .collect(Collectors.toMap(ItemDto::getId, i -> i));

        comments.forEach(comment -> itemDtos.get(comment.getItem().getId())
                .getComments().add(CommentMapper.toDto(comment)));

        return new ArrayList<>(itemDtos.values());
    }
}
