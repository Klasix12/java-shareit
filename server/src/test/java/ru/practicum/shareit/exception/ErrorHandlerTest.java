package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.controller.ErrorHandler;
import ru.practicum.shareit.item.controller.ItemController;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {ItemController.class, ErrorHandler.class})
public class ErrorHandlerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemController itemController;

    private final String error = "error";
    private final String description = "description";

    @Test
    void notFoundExceptionTest() throws Exception {
        when(itemController.getItem(anyLong()))
                .thenThrow(new NotFoundException(error, description));

        mvc.perform(get("/items/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void commentExceptionTest() throws Exception {
        when(itemController.getItem(anyLong()))
                .thenThrow(new CommentException(error, description));

        mvc.perform(get("/items/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidBookingDateExceptionTest() throws Exception {
        when(itemController.getItem(anyLong()))
                .thenThrow(new InvalidBookingDateException(error, description));

        mvc.perform(get("/items/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notAvailableExceptionTest() throws Exception {
        when(itemController.getItem(anyLong()))
                .thenThrow(new NotAvailableItemException(error, description));

        mvc.perform(get("/items/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userNotItemOwnerExceptionTest() throws Exception {
        when(itemController.getItem(anyLong()))
                .thenThrow(new UserNotItemOwnerException(error, description));

        mvc.perform(get("/items/1"))
                .andExpect(status().isForbidden());
    }
}
