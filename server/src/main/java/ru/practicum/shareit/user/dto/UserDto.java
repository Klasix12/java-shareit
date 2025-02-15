package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validators.OnCreate;
import ru.practicum.shareit.validators.OnUpdate;

@Data
@Builder
public class UserDto {
    private Long id;

    private String name;

    private String email;
}
