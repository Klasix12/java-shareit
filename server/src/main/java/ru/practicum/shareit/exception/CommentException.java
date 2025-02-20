package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {
    private final String description;

    public CommentException(String message, String description) {
        super(message);
        this.description = description;
    }
}
