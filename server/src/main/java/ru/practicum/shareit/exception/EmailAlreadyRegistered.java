package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyRegistered extends RuntimeException {

    private final String description;

    public EmailAlreadyRegistered(String message, String description) {
        super(message);
        this.description = description;
    }
}
