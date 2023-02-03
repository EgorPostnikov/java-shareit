package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM bookings ");
        jdbcTemplate.update("DELETE FROM items ");
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ITEMS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE BOOKINGS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void getBookingsOfUserAll() throws BadRequestException {
        UserDto user1 = new UserDto(null, "User1", "some1@email.com");
        UserDto user2 = new UserDto(null, "User2", "some2@email.com");
        ItemDto item1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto item2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        item1 = itemService.createItem(user2.getId(), item1);
        item2 = itemService.createItem(user2.getId(), item2);
        BookingShort booking1 = new BookingShort(null, item2.getId(), LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        BookingShort booking2 = new BookingShort(null, item1.getId(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(3));
        BookingDto savedBooking1 = bookingService.createBooking(user1.getId(), booking1);
        BookingDto savedBooking2 = bookingService.createBooking(user1.getId(), booking2);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> bookings = bookingService.getBookingsOfUser(user1.getId(), State.ALL, pageRequest);
        assertThat(bookings.size(), equalTo(2));
        List list = new ArrayList(bookings);
        BookingDto firstBooking = (BookingDto) list.get(0);
        assertThat(firstBooking.getId(), notNullValue());
        assertThat(firstBooking.getItem().getId(), equalTo(item1.getId()));
        assertThat(firstBooking.getBooker().getId(), equalTo(user1.getId()));

        bookings = bookingService.getBookingsOfUser(user1.getId(), State.PAST, pageRequest);
        assertThat(bookings.size(), equalTo(0));

        bookings = bookingService.getBookingsOfUser(user1.getId(), State.FUTURE, pageRequest);
        assertThat(bookings.size(), equalTo(2));

        pageRequest = PageRequest.of(0, 1, Sort.unsorted());

        bookings = bookingService.getBookingsOfUser(user1.getId(), State.ALL, pageRequest);
        assertThat(bookings.size(), equalTo(1));

    }

    @Test
    void getBookingsOfUserPast() throws BadRequestException {
        UserDto user1 = new UserDto(null, "User1", "some1@email.com");
        UserDto user2 = new UserDto(null, "User2", "some2@email.com");
        ItemDto item1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto item2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        item1 = itemService.createItem(user2.getId(), item1);
        item2 = itemService.createItem(user2.getId(), item2);
        BookingShort booking1 = new BookingShort(null, item2.getId(), LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        BookingShort booking2 = new BookingShort(null, item1.getId(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(3));
        BookingDto savedBooking1 = bookingService.createBooking(user1.getId(), booking1);
        BookingDto savedBooking2 = bookingService.createBooking(user1.getId(), booking2);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> bookings = bookingService.getBookingsOfUser(user1.getId(), State.PAST, pageRequest);
        assertThat(bookings.size(), equalTo(0));

    }

    @Test
    void getBookingsOfUserFutureSizeOneTakeSecond() throws BadRequestException {
        UserDto user1 = new UserDto(null, "User1", "some1@email.com");
        UserDto user2 = new UserDto(null, "User2", "some2@email.com");
        ItemDto item1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto item2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        item1 = itemService.createItem(user2.getId(), item1);
        item2 = itemService.createItem(user2.getId(), item2);
        BookingShort booking1 = new BookingShort(null, item2.getId(), LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        BookingShort booking2 = new BookingShort(null, item1.getId(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(3));
        BookingDto savedBooking1 = bookingService.createBooking(user1.getId(), booking1);
        BookingDto savedBooking2 = bookingService.createBooking(user1.getId(), booking2);

        PageRequest pageRequest = PageRequest.of(1, 1, Sort.unsorted());
        Collection<BookingDto> bookings = bookingService.getBookingsOfUser(user1.getId(), State.FUTURE, pageRequest);
        assertThat(bookings.size(), equalTo(1));
        List list = new ArrayList(bookings);
        BookingDto firstBooking = (BookingDto) list.get(0);
        assertThat(firstBooking.getId(), notNullValue());
        assertThat(firstBooking.getItem().getId(), equalTo(item2.getId()));
        assertThat(firstBooking.getBooker().getId(), equalTo(user1.getId()));

    }

    @Test
    void getBookingsOfUserCurrent() throws BadRequestException, InterruptedException {
        UserDto user1 = new UserDto(null, "User1", "some1@email.com");
        UserDto user2 = new UserDto(null, "User2", "some2@email.com");
        ItemDto item1 = new ItemDto(null, "Item1", "Item1 description", true,
                null, null, null);
        ItemDto item2 = new ItemDto(null, "Item2", "Item2 description", true,
                null, null, null);
        user1 = userService.createUser(user1);
        user2 = userService.createUser(user2);
        item1 = itemService.createItem(user2.getId(), item1);
        item2 = itemService.createItem(user2.getId(), item2);
        BookingShort booking1 = new BookingShort(null, item2.getId(), LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusMinutes(2));
        BookingShort booking2 = new BookingShort(null, item1.getId(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now().plusMinutes(3));
        BookingDto savedBooking1 = bookingService.createBooking(user1.getId(), booking1);
        BookingDto savedBooking2 = bookingService.createBooking(user1.getId(), booking2);
        TimeUnit.SECONDS.sleep(1);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> bookings = bookingService.getBookingsOfUser(user1.getId(), State.CURRENT, pageRequest);
        assertThat(bookings.size(), equalTo(1));
        List list = new ArrayList(bookings);
        BookingDto firstBooking = (BookingDto) list.get(0);
        assertThat(firstBooking.getId(), notNullValue());
        assertThat(firstBooking.getItem().getId(), equalTo(item2.getId()));
        assertThat(firstBooking.getBooker().getId(), equalTo(user1.getId()));
    }
}

