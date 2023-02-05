package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository repository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    private final User user1 = new User(
            1L,
            "John1",
            "john1@mail.com");
    private final User user2 = new User(
            2L,
            "John2",
            "john2@mail.com");
    private final User user3 = new User(
            3L,
            "John3",
            "john3@mail.com");
    private final Item item1 = new Item(
            1L,
            "item1",
            "Description",
            true,
            1L,
            null);

    @Test
    void verifyBootstrappingByPersistingAnBooking() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(new BookedItem());
        booking.setBooker(new Booker(1L));
        booking.setStatus(Status.WAITING);

        Assertions.assertNull(booking.getId());
        em.persist(booking);
        Assertions.assertNotNull(booking.getId());
    }

    @Test
    void findBookingByBooker_IdAndStatusOrderByStartDesc() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setItem(new BookedItem(1L, "Item1", 1L));
        booking1.setBooker(new Booker(3L));
        booking1.setStatus(Status.APPROVED);
        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.now().plusDays(4));
        booking2.setEnd(LocalDateTime.now().plusDays(5));
        booking2.setItem(new BookedItem(1L, "Item1", 1L));
        booking2.setBooker(new Booker(2L));
        booking2.setStatus(Status.APPROVED);
        Booking booking3 = new Booking();
        booking3.setStart(LocalDateTime.now().plusDays(7));
        booking3.setEnd(LocalDateTime.now().plusDays(8));
        booking3.setItem(new BookedItem(1L, "Item1", 1L));
        booking3.setBooker(new Booker(2L));
        booking3.setStatus(Status.APPROVED);
        Booking booking4 = new Booking();
        booking4.setStart(LocalDateTime.now().plusDays(10));
        booking4.setEnd(LocalDateTime.now().plusDays(11));
        booking4.setItem(new BookedItem(1L, "Item1", 1L));
        booking4.setBooker(new Booker(2L));
        booking4.setStatus(Status.APPROVED);
        repository.save(booking1);
        repository.save(booking2);
        repository.save(booking3);
        repository.save(booking4);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());

        Collection<Booking> bookings = repository.
                findBookingByBooker_IdAndEndBeforeOrderByStartDesc(2L, LocalDateTime.now(), pageRequest);
        assertThat(bookings.size(), equalTo(0));

        Collection<Booking> bookings2 = repository.
                findBookingByBooker_IdAndEndAfterOrderByStartDesc(2L, LocalDateTime.now(), pageRequest);
        assertThat(bookings2.size(), equalTo(3));
        assertThat(Arrays.stream(bookings2.toArray()).findFirst().get(), equalTo(booking4));
    }

}