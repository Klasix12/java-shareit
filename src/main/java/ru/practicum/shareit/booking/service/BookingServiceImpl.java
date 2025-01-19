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
import ru.practicum.shareit.exception.InvalidBookingDateException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserNotItemOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public BookingDto addBooking(Long userId, BookingRequestDto bookingRequestDto) {
        if (isInvalidBookingDate(bookingRequestDto)) {
            throw new InvalidBookingDateException("Ошибка бронирования", "Неверные даты бронирования");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь", "Не найден пользователь с id " + userId));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найден предмет", "Не найден предмет с id " + bookingRequestDto.getItemId()));
        if (!item.getAvailable()) {
            log.warn("Пользователь {} попытался забронировать недоступный предмет {}", userId, item.getId());
            throw new NotAvailableItemException("Невозможно забронировать предмет",
                    "Предмет " + item.getId() + " нельзя забронировать, т.к. он недоступен");
        }
        log.info("Пользователь {} забронировал предмет {}", userId, item.getId());
        return BookingMapper.toDto(bookingRepository.save(BookingMapper.toEntity(bookingRequestDto, user, item)));
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        Item item = booking.getItem();
        if (Objects.equals(userId, item.getOwner().getId())) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            log.warn("Пользователь {} попытался обновить статус вещи {}, не являясь ее владельцем", userId, item.getId());
            throw new UserNotItemOwnerException("Ошибка пользователя",
                    "Пользователь с id " + userId + " не является владельцем вещи");
        }
        log.info("Пользователь {} обновил статус предмета {}", userId, item.getId());
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrow(bookingId);
        if (Objects.equals(booking.getBooker().getId(), userId) ||
                Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.info("Пользователь с id {} получает информацию о бронировании {}", userId, bookingId);
            return BookingMapper.toDto(booking);
        }
        log.warn("Пользователь {} попытался получить информацию о бронировании {}," +
                " не являясь забронировавшим или владельцем", userId, booking);
        throw new UserNotItemOwnerException("Ошибка пользователя",
                "Пользователь с id " + userId + " не является владельцем вещи или бронирующим");
    }

    @Override
    public Collection<BookingDto> getUserBookings(Long userId, BookingState state) {
        userService.getUserByIdOrThrow(userId);
        List<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(userId, currentTime);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndAfter(userId, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartBefore(userId, currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                bookings = Collections.emptyList();
        }
        log.info("Пользователь {} получает список своих бронирований состояния {}", userId, state);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getOwnerBookingItems(Long userId, BookingState state) {
        userService.getUserByIdOrThrow(userId);
        List<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerId(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfter(userId, currentTime);
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndEndAfter(userId, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStartBefore(userId, currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                bookings = Collections.emptyList();
        }
        log.info("Пользователь {} получает список своих вещей состояния {}", userId, state);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдено бронирование", "Не найдено бронирование с id " + bookingId));
    }

    private Boolean isInvalidBookingDate(BookingRequestDto bookingRequestDto) {
        return bookingRequestDto.getStart().equals(bookingRequestDto.getEnd()) ||
                bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()) ||
                bookingRequestDto.getStart().isBefore(LocalDateTime.now());
    }
}
