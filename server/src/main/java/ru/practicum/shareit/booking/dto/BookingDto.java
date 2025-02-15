package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private UserDto booker;
    private ItemDto item;
    private BookingStatus status;
    private LocalDateTime start;
    private LocalDateTime end;
}
