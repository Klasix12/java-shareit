package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentRequestDto {
    @NotEmpty
    private String text;

    private LocalDateTime created;
}
