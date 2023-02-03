package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.controller.BookingController;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDto booking1 = new BookingDto(
            1L,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(2),
            new BookedItem(2L, "Item1", 3L),
            new Booker(4L),
            Status.APPROVED);

    private BookingDto booking2 = new BookingDto(
            5L,
            LocalDateTime.now().plusMinutes(3),
            LocalDateTime.now().plusMinutes(4),
            new BookedItem(6L, "Item2", 7L),
            new Booker(8L),
            Status.WAITING);
    @Test
    void createBooking() throws Exception {
        Long userId = 99L;
        when(bookingService.createBooking(eq(99L), any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking1.getItem().getName())))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        Long userId = 99L;
        when(bookingService.updateBooking(eq(2L), eq(99L), eq(true)))
                .thenReturn(booking1);

        mvc.perform(patch("/bookings/2")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking1.getItem().getName())))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
    }

    @Test
    void getBookingById() throws Exception {
        Long userId = 99L;
        when(bookingService.getBookingById(eq(2L), eq(99L)))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/2")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(booking1.getItem().getName())))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString())));
    }

    @Test
    void getBookingsOfUser() throws Exception {
        Collection<BookingDto> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        Long userId = 99L;
        when(bookingService.getBookingsOfUser(eq(99L), eq(State.ALL), any()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(booking1.getItem().getName())))
                .andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", is(booking2.getId()), Long.class))
                .andExpect(jsonPath("$[1].item.name", is(booking2.getItem().getName())))
                .andExpect(jsonPath("$[1].item.id", is(booking2.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[1].booker.id", is(booking2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[1].status", is(booking2.getStatus().toString())));
    }

    @Test
    void getBookingsOfUsersItems() throws Exception {
        Collection<BookingDto> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        Long userId = 99L;
        when(bookingService.getBookingsOfUsersItems(eq(99L), eq(State.ALL), any()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(booking1.getItem().getName())))
                .andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", is(booking2.getId()), Long.class))
                .andExpect(jsonPath("$[1].item.name", is(booking2.getItem().getName())))
                .andExpect(jsonPath("$[1].item.id", is(booking2.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[1].booker.id", is(booking2.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[1].status", is(booking2.getStatus().toString())));
    }

    @Test
    void handleExceptionTestNotFound() throws Exception {
        Long userId = 99L;
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new NoSuchElementException("Message"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message")));
    }

    @Test
    void handleExceptionBadRequest() throws Exception {
        Long userId = 99L;
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new EntityNotFoundException("Message"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Message")));
    }

    @Test
    void handleExceptionBadRequest2() throws Exception {
        Long userId = 99L;
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new BadRequestException("Message"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Message")));
    }

}


