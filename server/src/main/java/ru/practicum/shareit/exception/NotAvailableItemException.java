package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class NotAvailableItemException extends RuntimeException {
    private final String description;

    public NotAvailableItemException(String message, String description) {
        super(message);
        this.description = description;
    }
}
