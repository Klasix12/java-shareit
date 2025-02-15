package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull
    private Long itemId;

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;
}
