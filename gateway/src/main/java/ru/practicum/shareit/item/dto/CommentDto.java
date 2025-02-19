package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    private Long itemId;
    @NotBlank
    private String authorName;
    @NotBlank
    @Size(max = 2048)
    private String text;
    private LocalDateTime created;
}
