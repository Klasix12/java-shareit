package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(userIdHeader) Long userId,
                                 @Valid @RequestBody BookingRequestDto bookingDto) {
        log.trace("Добавление бронирования");
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader(userIdHeader) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        log.trace("Обновление бронирования");
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(userIdHeader) Long userId,
                                 @PathVariable Long bookingId) {
        log.trace("Получение бронирования");
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getBookings(@RequestHeader(userIdHeader) Long userId,
                                              @RequestParam(defaultValue = "ALL") BookingState state) {
        log.trace("Получение бронирований пользователя");
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingItems(@RequestHeader(userIdHeader) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {
        log.trace("Получение владельцем его бронирований");
        return bookingService.getOwnerBookingItems(userId, state);
    }
}
