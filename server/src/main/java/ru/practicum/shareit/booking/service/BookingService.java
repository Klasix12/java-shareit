package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDto addBooking(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    Collection<BookingDto> getUserBookings(Long userId, BookingState state);

    Collection<BookingDto> getOwnerBookingItems(Long userId, BookingState state);
}
