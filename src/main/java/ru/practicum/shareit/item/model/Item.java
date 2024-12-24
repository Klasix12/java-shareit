package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;


@Data
@Builder
public class Item {
    private Long id;

    private String name;

    private String description;

    private User owner;

    private Boolean available;
}
