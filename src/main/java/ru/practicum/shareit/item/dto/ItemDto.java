package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.OnCreate;

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
}
