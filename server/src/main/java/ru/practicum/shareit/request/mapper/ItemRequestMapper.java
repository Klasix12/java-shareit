package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequest toEntity(ItemRequestDto dto, User user) {
        return ItemRequest.builder()
                .id(dto.getId())
                .requester(user)
                .description(dto.getDescription())
                .created(dto.getCreated())
                .build();
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .userId(request.getRequester().getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static List<ItemRequestDto> toDto(List<ItemRequest> requests) {
        return requests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());

    }
}
