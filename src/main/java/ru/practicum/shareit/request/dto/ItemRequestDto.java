package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private Long id;

    private Long userId;

    @NotNull(message = "description")
    private String description;

    private List<ItemDto> items;

    private LocalDateTime created;
}
