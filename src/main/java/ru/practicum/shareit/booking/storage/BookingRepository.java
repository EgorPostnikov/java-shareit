package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Collection<Booking> findBookingByBooker_IdOrderByStartDesc (Long bookerId);
    Collection<Booking> findBookingByBooker_IdAndStatus (Long bookerId, Status status);
    Collection<Booking> findBookingByBooker_IdAndEndBefore (Long bookerId, LocalDateTime time);
    Collection<Booking> findBookingByBooker_IdAndEndAfter (Long bookerId, LocalDateTime time);
    Collection<Booking> findBookingByBooker_IdAndEndAfterAndStartBefore (Long bookerId, LocalDateTime time,LocalDateTime time2);
    Collection<Booking> findBookingByItem_OwnerOrderByStartDesc (Long owner);
    Collection<Booking> findBookingByItem_OwnerAndStatus (Long owner, Status status);
    Collection<Booking> findBookingByItem_OwnerAndEndBefore (Long owner, LocalDateTime time);
    Collection<Booking> findBookingByItem_OwnerAndEndAfter (Long owner, LocalDateTime time);
    Collection<Booking> findBookingByItem_OwnerAndEndAfterAndStartBefore (Long owner, LocalDateTime time,LocalDateTime time2);

    List<Booking> findBookingByItem_IdAndEndIsBeforeOrderByEndDesc (Long itemId, LocalDateTime time);
    List<Booking> findBookingByItem_IdAndStartIsAfterOrderByStart (Long itemId,LocalDateTime time);
}