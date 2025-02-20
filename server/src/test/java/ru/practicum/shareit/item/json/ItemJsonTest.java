package ru.practicum.shareit.item.json;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJsonTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void testItemJson() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .authorName("author")
                .text("text")
                .created(LocalDateTime.now())
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .requestId(1L)
                .comments(List.of(commentDto))
                .available(true)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.available").isEqualTo(itemDto.getAvailable());

        assertThat(result).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathValue("$.comments.[0].created").isNotNull();

    }
}
