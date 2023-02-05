package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositorylTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final ItemService itemService;


    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM items ");
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void getItemByIdTest() {
        UserDto userDto1 = new UserDto(null, "User1", "some1@email.com");
        UserDto userDto2 = new UserDto(null, "User2", "some2@email.com");
        ItemDto itemDto1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto itemDto2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);
        userDto2 = userService.createUser(userDto2);
        itemService.createItem(userDto2.getId(), itemDto1);
        itemService.createItem(userDto2.getId(), itemDto2);

        ItemDtoWithComments item = itemService.getItemById(1L, 1L);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto1.getName()));
        assertThat(item.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemDto1.getAvailable()));
        assertThat(item.getLastBooking(), nullValue());
        assertThat(item.getNextBooking(), nullValue());
        assertThat(item.getComments().size(), equalTo(0));
    }
}
