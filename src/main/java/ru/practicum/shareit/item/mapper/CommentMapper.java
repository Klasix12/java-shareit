package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    private CommentMapper() {}

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .itemId(comment.getItem().getId())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toEntity(CommentRequestDto commentRequestDto, User user, Item item) {
        return Comment.builder()
                .item(item)
                .user(user)
                .text(commentRequestDto.getText())
                .created(commentRequestDto.getCreated())
                .build();
    }
}
