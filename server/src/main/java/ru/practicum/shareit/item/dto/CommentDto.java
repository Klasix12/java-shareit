package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private Long itemId;
    private String authorName;
    private String text;
    private LocalDateTime created;
}
