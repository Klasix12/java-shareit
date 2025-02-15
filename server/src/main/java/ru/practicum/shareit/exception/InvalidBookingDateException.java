package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class InvalidBookingDateException extends RuntimeException {
    private final String description;

    public InvalidBookingDateException(String message, String description) {
        super(message);
        this.description = description;
    }
}
