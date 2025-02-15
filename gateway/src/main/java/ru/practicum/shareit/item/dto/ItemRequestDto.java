package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.OnCreate;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotEmpty(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    private String description;

    @NotNull(groups = OnCreate.class)
    private Boolean available;

    private Long requestId;
}
