package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.OnCreate;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;

    @NotEmpty(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    private String description;

    @NotNull(groups = OnCreate.class)
    private Boolean available;

    private List<CommentDto> comments;

    private String lastBooking;

    private String nextBooking;
}
