package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestRepository itemRequestRepository;

    private ItemRequestDto request1 = new ItemRequestDto(
            null,
            "Description1",
            1L,
            LocalDateTime.now(),
            new HashSet<>());
    private ItemRequestDto request2 = new ItemRequestDto(
            null,
            "Description2",
            1L,
            LocalDateTime.now(),
            new HashSet<>());
    private UserDto userDto1 = new UserDto(
            null,
            "User1",
            "some1@email.com");
    private UserDto userDto2 = new UserDto(
            null,
            "User2",
            "some2@email.com");
    private ItemDto itemDto1 = new ItemDto(
            null,
            "Item1",
            "Item1 description",
            true,
            null,
            null,
            null);
    private ItemDto itemDto2 = new ItemDto(
            null,
            "Item2",
            "Item2 description",
            true,
            null,
            null,
            null);

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM items ");
        jdbcTemplate.update("DELETE FROM requests ");
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE REQUESTS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1");
        UserDto user1 = userService.createUser(userDto1);
        userService.createUser(userDto2);
        itemService.createItem(user1.getId(), itemDto1);
        itemService.createItem(user1.getId(), itemDto2);
    }

    @Test
    void getItemByIdTest() {
        ItemRequest request = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now(),
                null);

        ItemRequest gotRequest = itemRequestRepository.save(request);
        assertThat(gotRequest.getId(), notNullValue());
        assertThat(gotRequest.getDescription(), equalTo(request.getDescription()));
        assertThat(gotRequest.getRequestor(), equalTo(request.getRequestor()));
        assertThat(gotRequest.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void findItemRequestsByRequestor() {
        ItemRequest request = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now().plusSeconds(1),
                null);
        ItemRequest request2 = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now(),
                null);
        itemRequestRepository.save(request);
        itemRequestRepository.save(request2);

        Collection<ItemRequest> requests = itemRequestRepository
                .findItemRequestsByRequestorOrderByCreatedDesc(2L);

        assertThat(requests.size(), equalTo(2));
        ItemRequest gotRequest = requests.stream().findFirst().get();
        assertThat(gotRequest.getId(), notNullValue());
        assertThat(gotRequest.getDescription(), equalTo(request.getDescription()));
        assertThat(gotRequest.getRequestor(), equalTo(request.getRequestor()));
        assertThat(gotRequest.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void findItemRequestsByRequestor2() {
        ItemRequest request = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now().plusSeconds(1),
                null);
        ItemRequest request2 = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now(),
                null);

        itemRequestRepository.save(request);
        itemRequestRepository.save(request2);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());

        Collection<ItemRequest> requests = itemRequestRepository
                .findItemRequestsByRequestorNotOrderByCreatedDesc(1L, pageRequest);

        assertThat(requests.size(), equalTo(2));
        ItemRequest gotRequest = requests.stream().findFirst().get();
        assertThat(gotRequest.getId(), notNullValue());
        assertThat(gotRequest.getDescription(), equalTo(request.getDescription()));
        assertThat(gotRequest.getRequestor(), equalTo(request.getRequestor()));
        assertThat(gotRequest.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void findById() {
        ItemRequest request = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now().plusSeconds(1),
                null);
        ItemRequest request2 = new ItemRequest(
                null,
                "Description",
                1L,
                LocalDateTime.now(),
                null);

        itemRequestRepository.save(request);
        itemRequestRepository.save(request2);

        ItemRequest gotRequest = itemRequestRepository
                .findById(1L).get();

        assertThat(gotRequest.getId(), notNullValue());
        assertThat(gotRequest.getDescription(), equalTo(request.getDescription()));
        assertThat(gotRequest.getRequestor(), equalTo(request.getRequestor()));
        assertThat(gotRequest.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void existsById() {
        ItemRequest request = new ItemRequest(
                null,
                "Description",
                2L,
                LocalDateTime.now().plusSeconds(1),
                null);
        ItemRequest request2 = new ItemRequest(
                null,
                "Description",
                1L,
                LocalDateTime.now(),
                null);
        itemRequestRepository.save(request);
        itemRequestRepository.save(request2);

        Boolean isExist = itemRequestRepository.existsById(1L);

        assertThat(isExist, equalTo(true));
    }
}
