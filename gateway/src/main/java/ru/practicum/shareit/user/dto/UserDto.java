package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validator.OnCreate;
import ru.practicum.shareit.validator.OnUpdate;

@Data
@Builder
public class UserDto {
    private Long id;

    @NotNull(groups = OnCreate.class)
    private String name;

    @NotEmpty(groups = OnCreate.class)
    @Email(message = "Неверный формат email", groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
