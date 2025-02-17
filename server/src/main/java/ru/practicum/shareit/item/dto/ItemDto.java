package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

    private List<CommentDto> comments;

    private String lastBooking;

    private String nextBooking;

    public boolean hasRequestId() {
        return requestId != null && requestId != 0;
    }
}
