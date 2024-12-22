package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validators.OnCreate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;

    @NotEmpty(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    private String description;

    private User owner;

    @NotNull(groups = OnCreate.class)
    private Boolean available;
}
