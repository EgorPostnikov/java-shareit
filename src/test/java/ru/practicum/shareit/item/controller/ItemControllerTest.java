package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.service.ItemService;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @Autowired
    private MockMvc mvc;
    private final CommentDto comment1 = new CommentDto(
            5L,
            "Commentaries",
            6L,
            7L,
            "Autor");
    private final ItemDto item1 = new ItemDto(
            1L,
            "Item1",
            "Description1",
            true,
            2L,
            new ItemBookingDto(11L, 12L),
            new ItemBookingDto(13L, 14L));
    private final ItemDtoWithComments item2 = new ItemDtoWithComments(
            3L,
            "Item2",
            "Description2",
            true,
            4L,
            new ItemBookingDto(31L, 32L),
            new ItemBookingDto(33L, 34L),
            new ArrayList<>());


    @Test
    void createItem() throws Exception {
        Long userId = 99L;
        when(itemService.createItem(eq(99L), any()))
                .thenReturn(item1);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(item1.getRequestId()), Long.class))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.lastBooking.id", is(item1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(item1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(item1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(item1.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    void getItemById() throws Exception {
        Long userId = 99L;
        when(itemService.getItemById(eq(2L), eq(99L)))
                .thenReturn(item2);

        mvc.perform(get("/items/2")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item2.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item2.getName())))
                .andExpect(jsonPath("$.available", is(item2.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(item2.getRequestId()), Long.class))
                .andExpect(jsonPath("$.description", is(item2.getDescription())))
                .andExpect(jsonPath("$.lastBooking.id", is(item2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(item2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(item2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(item2.getNextBooking().getBookerId()), Long.class));

    }

    @Test
    void updateItem() throws Exception {
        Long userId = 99L;
        when(itemService.updateItem(eq(99L), any()))
                .thenReturn(item1);

        mvc.perform(patch("/items/2")
                        .content(mapper.writeValueAsString(item1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(item1.getRequestId()), Long.class))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.lastBooking.id", is(item1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(item1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(item1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(item1.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    void deleteItem() throws Exception {
        mvc.perform(delete("/items/2")
                        .content(mapper.writeValueAsString(item1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItems() throws Exception {
        Collection<ItemDtoWithComments> items = new ArrayList<>();
        items.add(item2);
        Long userId = 99L;
        when(itemService.getItems(eq(99L), any()))
                .thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item2.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item2.getName())))
                .andExpect(jsonPath("$[0].available", is(item2.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(item2.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(item2.getDescription())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(item2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(item2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(item2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(item2.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    void searchItems() throws Exception {
        Collection<ItemDto> items = new ArrayList<>();
        items.add(item1);
        when(itemService.searchItems(eq("SearchingText")))
                .thenReturn(items);

        mvc.perform(get("/items/search")
                        .param("text", "SearchingText")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item1.getName())))
                .andExpect(jsonPath("$[0].available", is(item1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(item1.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(item1.getDescription())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(item1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(item1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(item1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(item1.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    void createComment() throws Exception {
        Long userId = 99L;
        when(itemService.createComment(any()))
                .thenReturn(comment1);

        mvc.perform(post("/items/2/comment")
                        .content(mapper.writeValueAsString(comment1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment1.getText())))
                .andExpect(jsonPath("$.item", is(comment1.getItem()), Long.class))
                .andExpect(jsonPath("$.authorId", is(comment1.getAuthorId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(comment1.getAuthorName())));
    }
    @Test
    void createComment2() throws Exception {
        CommentDto commentDto = comment1;
        commentDto.setText("  ");
        Long userId = 99L;
        when(itemService.createComment(any()))
                .thenReturn(comment1);

        mvc.perform(post("/items/2/comment")
                        .content(mapper.writeValueAsString(comment1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleExceptionTestNotFound() throws Exception {
        Long userId = 99L;
        when(itemService.createItem(eq(99L), any()))
                .thenThrow(new NoSuchElementException("Message"));

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message")));
    }

    @Test
    void handleExceptionTestBadRequest() throws Exception {
        Long userId = 99L;
        when(itemService.createItem(eq(99L), any()))
                .thenThrow(new EntityNotFoundException("Message"));

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Message")));
    }

}

