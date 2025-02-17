package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final BookingRepository bookingRepository;

    private final BookingService service;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private User user;

    private Item item;

    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("email@email.com")
                .name("name")
                .build());
        item = itemRepository.save(Item.builder()
                .name("name")
                .description("desc")
                .owner(user)
                .available(true)
                .build());
        bookingRequestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void addBookingTest() {
        Long bookingId = service.addBooking(user.getId(), bookingRequestDto).getId();
        Booking savedBooking = bookingRepository.findById(bookingId)
                .orElseThrow();

        assertThatBookingHasCorrectFields(savedBooking);
    }

    @Test
    void updateBookingStatusTest() {
        Long bookingId = service.addBooking(user.getId(), bookingRequestDto).getId();
        Booking savedBooking = bookingRepository.findById(bookingId)
                .orElseThrow();
        assertThat(savedBooking.getStatus(), equalTo(BookingStatus.WAITING));
        service.updateBookingStatus(user.getId(), bookingId, true);
        assertThat(savedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void getBooking() {
        Long bookingId = service.addBooking(user.getId(), bookingRequestDto).getId();
        Booking savedBooking = bookingRepository.findById(bookingId)
                .orElseThrow();
        assertThatBookingHasCorrectFields(savedBooking);
    }

    @Test
    void getUserBookings() {
        int savedItemsCount = 3;
        for (int i = 0; i < savedItemsCount; i++) {
            service.addBooking(user.getId(), bookingRequestDto);
        }

        User user2 = userRepository.save(User.builder()
                .email("email2@email.com")
                .name("name")
                .build());
        for (int i = 0; i < savedItemsCount; i++) {
            service.addBooking(user2.getId(), bookingRequestDto);
        }

        Collection<BookingDto> userBookings = service.getUserBookings(user.getId(), BookingState.ALL);
        assertThat(userBookings.size(), equalTo(savedItemsCount));
        for (BookingDto b : userBookings) {
            assertThat(b.getBooker().getId(), equalTo(user.getId()));
        }
    }

    @Test
    void getOwnerBookings() {
        int savedItemsCount = 3;
        for (int i = 0; i < savedItemsCount; i++) {
            service.addBooking(user.getId(), bookingRequestDto);
        }

        User user2 = userRepository.save(User.builder()
                .email("email2@email.com")
                .name("name")
                .build());

        assertThat(service.getOwnerBookings(user.getId(), BookingState.ALL).size(), equalTo(savedItemsCount));
        assertThat(service.getOwnerBookings(user2.getId(), BookingState.ALL).size(), equalTo(0));
    }

    private void assertThatBookingHasCorrectFields(Booking booking) {
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getBooker(), equalTo(user));
        assertThat(booking.getItem(), equalTo(item));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(booking.getStart(), equalTo(bookingRequestDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingRequestDto.getEnd()));
    }
}

