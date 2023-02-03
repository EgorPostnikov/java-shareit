package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto request1 = new ItemRequestDto(
            1L,
            "Description1",
            2L,
            LocalDateTime.now(),
            new HashSet<>());
    private ItemRequestDto request2 = new ItemRequestDto(
            3L,
            "Description2",
            4L,
            LocalDateTime.now(),
            new HashSet<>());

    @Test
    void createItemRequest() throws Exception {
        Long userId = 99L;
        when(itemRequestService.createItemRequest(eq(99L), any()))
                .thenReturn(request1);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request1))
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.requestor", is(request1.getRequestor()), Long.class));
    }


    @Test
    void getItemRequests() throws Exception {
        Collection<ItemRequestDto> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        Long userId = 99L;
        when(itemRequestService.getItemRequests(eq(99L)))
                .thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request1.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(request1.getRequestor()), Long.class))
                .andExpect(jsonPath("$[1].id", is(request2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(request2.getDescription())))
                .andExpect(jsonPath("$[1].requestor", is(request2.getRequestor()), Long.class));
    }

    @Test
    void getAnotherItemRequests() throws Exception {
        Collection<ItemRequestDto> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        Long userId = 99L;
        when(itemRequestService.getAnotherItemRequests(eq(99L), any()))
                .thenReturn(requests);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request1.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(request1.getRequestor()), Long.class))
                .andExpect(jsonPath("$[1].id", is(request2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(request2.getDescription())))
                .andExpect(jsonPath("$[1].requestor", is(request2.getRequestor()), Long.class));
    }

    @Test
    void getItemRequestById() throws Exception {
        Long userId = 99L;
        when(itemRequestService.getItemRequestById(eq(2L), any()))
                .thenReturn(request1);

        mvc.perform(get("/requests/2")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request1.getDescription())))
                .andExpect(jsonPath("$.requestor", is(request1.getRequestor()), Long.class));
    }

    @Test
    void handleExceptionTestNotFound() throws Exception {
        Long userId = 99L;
        when(itemRequestService.getItemRequestById(eq(2L), any()))
                .thenThrow(new NoSuchElementException("Message"));

        mvc.perform(get("/requests/2")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message")));
    }

    @Test
    void handleExceptionValidation() throws Exception {
        Long userId = 99L;
        when(itemRequestService.getItemRequestById(eq(2L), any()))
                .thenThrow(new EntityNotFoundException("Message"));

        mvc.perform(get("/requests/2")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Message")));
    }
}
