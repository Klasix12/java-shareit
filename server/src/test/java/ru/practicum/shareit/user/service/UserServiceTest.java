package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserRepository repository;
    private final UserService service;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
    }

    @Test
    void addUserTest() {
        UserDto savedUserDto = service.addUser(userDto);
        User user = repository.findById(savedUserDto.getId())
                .orElseThrow();
        assertThatUserHasCorrectFields(user);
    }

    @Test
    void getUserByIdOrThrowTest() {
        UserDto savedUserDto = service.addUser(userDto);
        UserDto user = service.getUserByIdOrThrow(savedUserDto.getId());
        assertThatUserHasCorrectFields(user);
    }

    @Test
    void updateUserTest() {
        UserDto savedUserDto = service.addUser(userDto);
        savedUserDto.setEmail("email123@321.com");
        UserDto updatedUser = service.updateUser(savedUserDto.getId(), savedUserDto);
        assertThat(repository.findById(savedUserDto.getId()).get().getEmail(), equalTo(updatedUser.getEmail()));
    }

    @Test
    void deleteUserTest() {
        UserDto savedUserDto = service.addUser(userDto);
        service.deleteUser(savedUserDto.getId());
        assertThat(repository.findById(savedUserDto.getId()), equalTo(Optional.empty()));
    }

    void assertThatUserHasCorrectFields(User user) {
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    void assertThatUserHasCorrectFields(UserDto user) {
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }
}
