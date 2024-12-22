package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validators.OnCreate;
import ru.practicum.shareit.validators.OnUpdate;

@Data
public class User {
    private Long id;

    @NotNull(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    @Email(message = "Неверный формат email", groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
