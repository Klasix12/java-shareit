package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserNotItemOwnerException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto addBooking(Long userId, BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getStart().equals(bookingRequestDto.getEnd()) ||
                bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart())) {
            throw new RuntimeException();
        }
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        Item item = ItemMapper.toEntity(itemService.getItemByIdOrThrow(bookingRequestDto.getItemId()), user);
        if (item.getAvailable()) {
            log.info("Пользователь {} забронировал предмет {}", user, item);
            return BookingMapper.toDto(bookingRepository.save(BookingMapper.toEntity(bookingRequestDto, user, item)));
        }
        log.warn("Пользователь {} попытался забронировать недоступный предмет {}", user, item);
        throw new NotAvailableItemException("Невозможно забронировать предмет",
                "Этот предмет нельзя забронировать, т.к. он недоступен");
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        Item item = booking.getItem();
        if (userId == item.getOwner().getId()) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            log.warn("Пользователь {} попытался обновить статус вещи {}, не являясь ее владельцем", user, item);
            throw new UserNotItemOwnerException("Ошибка пользователя",
                    "Пользователь с id " + userId + " не является владельцем вещи");
        }
        log.info("Пользователь {} обновил статус предмета {}", user, item);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrow(bookingId);
        if (booking.getBooker().getId() == userId ||
                booking.getItem().getOwner().getId() == userId) {
            log.info("Пользователь с id {} получает информацию о бронировании {}", userId, booking);
            return BookingMapper.toDto(booking);
        }
        log.warn("Пользователь {} попытался получить информацию о бронировании {}, не являясь забронировавшим или владельцем", userId, booking);
        throw new UserNotItemOwnerException("Ошибка пользователя",
                "Пользователь с id " + userId + " не является владельцем вещи или бронирующим");
    }

    @Override
    public Collection<BookingDto> getUserBookings(Long userId, BookingState state) {
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker(user);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user, Instant.now(), Instant.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerAndEndAfter(user, Instant.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerAndStartBefore(user, Instant.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerAndStatus(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerAndStatus(user, BookingStatus.REJECTED);
                break;
            default:
                bookings = Collections.emptyList();
        }
        log.info("Пользователь {} получает список своих бронирований состояния {}", user, state);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getOwnerBookingItems(Long userId, BookingState state) {
        User user = UserMapper.toEntity(userService.getUserByIdOrThrow(userId));
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwner(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerAndStartBeforeAndEndAfter(userId, Instant.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerAndEndAfter(userId, Instant.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findALlByOwnerAndStartBefore(userId, Instant.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerAndStatus(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                bookings = Collections.emptyList();
        }
        log.info("Пользователь {} получает список своих вещей состояния {}", user, state);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бронирование", "Не найдено бронирование с id " + bookingId));
    }
}
