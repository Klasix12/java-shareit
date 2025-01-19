package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequestDto {
    @NotEmpty
    private String text;

    private LocalDateTime created;
}
