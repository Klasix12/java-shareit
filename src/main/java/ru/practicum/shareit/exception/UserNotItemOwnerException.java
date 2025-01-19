package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class UserNotItemOwnerException extends RuntimeException {

    private final String description;

    public UserNotItemOwnerException(String message, String description) {
        super(message);
        this.description = description;
    }
}
