package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final UserRepository userRepository;

    private final ItemRequestService service;

    private final ItemRequestRepository itemRequestRepository;

    private User user;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("email@email.com")
                .name("name")
                .build());
        requestDto = ItemRequestDto.builder()
                .userId(user.getId())
                .description("desc")
                .created(LocalDateTime.of(2025, 1, 1, 1, 1))
                .build();
    }

    @Test
    void addItemRequestTest() {
        ItemRequestDto savedRequestDto = service.save(requestDto);
        ItemRequest savedRequest = itemRequestRepository.findById(savedRequestDto.getId())
                .orElseThrow();

        assertThatItemHasCorrectFields(savedRequest);
    }

    @Test
    void getUserRequestsTest() {
        int savedItemsCount = 3;
        for (int i = 0; i < savedItemsCount; i++) {
            service.save(requestDto);
        }

        List<ItemRequestDto> savedItems = service.getUserRequests(user.getId());

        assertThat(savedItems.size(), equalTo(savedItemsCount));
        assertThatItemHasCorrectFields(savedItems.get(0));
    }

    @Test
    void getAllRequests() {
        int savedItemsCount = 3;
        for (int i = 0; i < savedItemsCount; i++) {
            service.save(requestDto);
        }

        User user2 = userRepository.save(User.builder()
                .email("email2@email.com")
                .name("name2")
                .build());
        ItemRequestDto requestDto2 = ItemRequestDto.builder()
                .userId(user2.getId())
                .created(LocalDateTime.now())
                .description("desc2")
                .build();
        for (int i = 0; i < savedItemsCount; i++) {
            service.save(requestDto2);
        }

        List<ItemRequestDto> savedItems = service.getAll();
        assertThat(savedItems.size(), equalTo(savedItemsCount * 2));
        assertThatItemHasCorrectFields(savedItems.get(0));
    }

    @Test
    void getById() {
        ItemRequestDto savedRequestDto = service.save(requestDto);
        ItemRequestDto savedRequest = service.getById(savedRequestDto.getId());
        assertThatItemHasCorrectFields(savedRequest);
    }

    private void assertThatItemHasCorrectFields(ItemRequest request) {
        assertThat(request.getId(), notNullValue());
        assertThat(request.getRequester().getId(), equalTo(user.getId()));
        assertThat(request.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(request.getCreated(), notNullValue());
    }

    private void assertThatItemHasCorrectFields(ItemRequestDto request) {
        assertThat(request.getId(), notNullValue());
        assertThat(request.getUserId(), equalTo(user.getId()));
        assertThat(request.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(request.getCreated(), notNullValue());
    }
}
