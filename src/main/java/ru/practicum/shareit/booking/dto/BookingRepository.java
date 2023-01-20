package ru.practicum.shareit.booking.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query(" select b from Booking b where b.booker.id = ?1 order by b.end desc ")
    Collection<Booking> getBookingsOfUser(Long userId, String state);

    @Query(" select b from Booking b where b.item.id = ?1 order by b.end desc")
    Collection<Booking> getBookingsOfUsersItems(Long userId, String state);
}