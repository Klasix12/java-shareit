package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserNotItemOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository repository;
    private final ItemService service;
    private ItemDto itemDto;
    private CommentRequestDto commentRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("email@email.com")
                .name("name")
                .build());

        itemDto = ItemDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .build();

        commentRequestDto = new CommentRequestDto("text", LocalDateTime.now());
    }

    @Test
    void addItemTest() {
        ItemDto savedItemDto = service.addItem(user.getId(), itemDto);
        Item savedItem = repository.findById(savedItemDto.getId())
                .orElseThrow();
        assertThatItemHasCorrectFields(savedItem);
    }

    @Test
    void updateItemTest() {
        ItemDto savedItemDto = service.addItem(user.getId(), itemDto);
        Item savedItem = repository.findById(savedItemDto.getId())
                .orElseThrow();
        assertThatItemHasCorrectFields(savedItem);
        savedItemDto.setAvailable(false);
        ItemDto updatedItem = service.updateItem(user.getId(), savedItemDto, savedItemDto.getId());
        assertThat(updatedItem.getAvailable(), equalTo(false));
    }

    @Test
    void updateItemNotFoundTest() {
        assertThrows(NotFoundException.class, () ->
                service.updateItem(user.getId(), itemDto, 1L));
    }

    @Test
    void updateItemEmptyFieldsTest() {
        ItemDto savedItemDto = service.addItem(user.getId(), itemDto);
        ItemDto updatedItem = service.updateItem(user.getId(), new ItemDto(), savedItemDto.getId());
        assertThat(savedItemDto, equalTo(updatedItem));
    }

    @Test
    void updateUserNotOwnerTest() {
        ItemDto savedItem = service.addItem(user.getId(), itemDto);
        User user2 = userRepository.save(User.builder()
                .email("email2@email2.com")
                .name("name")
                .build());
        assertThrows(UserNotItemOwnerException.class, () ->
                service.updateItem(user2.getId(), itemDto, savedItem.getId()));
    }

    @Test
    void getItemAndCommentsOrThrowTest() {
        Item savedItem = repository.save(ItemMapper.toEntity(itemDto, user));
        createBooking(savedItem, user);
        CommentDto commentDto = service.addComment(user.getId(), savedItem.getId(), commentRequestDto);
        service.addComment(user.getId(), savedItem.getId(), new CommentRequestDto("text2", LocalDateTime.now()));
        ItemDto itemDtoWithComments = service.getItemAndCommentsOrThrow(savedItem.getId());
        assertThat(itemDtoWithComments.getComments().size(), equalTo(2));
        assertThat(itemDtoWithComments.getComments().get(0), equalTo(commentDto));
    }

    @Test
    void getUserItemsTest() {
        int savedItemsCount = 3;
        for (int i = 0; i < savedItemsCount; i++) {
            service.addItem(user.getId(), itemDto);
        }

        User user2 = userRepository.save(User.builder()
                .email("email2@email.com")
                .name("name2")
                .build());

        for (int i = 0; i < savedItemsCount; i++) {
            service.addItem(user2.getId(), itemDto);
        }
        List<ItemDto> items = service.getUserItems(user.getId());
        assertThat(items.size(), equalTo(savedItemsCount));
    }

    @Test
    void searchItemsTest() {
        service.addItem(user.getId(), itemDto);
        List<ItemDto> items = service.searchItems(itemDto.getName());

        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getName(), equalTo(itemDto.getName()));
    }

    @Test
    void addCommentTest() {
        Item savedItem = repository.save(ItemMapper.toEntity(itemDto, user));
        createBooking(savedItem, user);
        CommentDto commentDto = service.addComment(user.getId(), savedItem.getId(), commentRequestDto);
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow();
        assertThat(comment.getId(), equalTo(commentDto.getId()));
        assertThat(comment.getText(), equalTo(commentDto.getText()));
        assertThat(comment.getCreated(), equalTo(commentDto.getCreated()));
    }

    @Test
    void addCommentWithoutBookingTest() {
        Item savedItem = repository.save(ItemMapper.toEntity(itemDto, user));
        assertThrows(CommentException.class,
                () -> service.addComment(user.getId(), savedItem.getId(), new CommentRequestDto("text", LocalDateTime.now())));
    }

    private void createBooking(Item item, User user) {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build());
    }

    private void assertThatItemHasCorrectFields(Item item) {
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }
}
