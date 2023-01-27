package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findBookingByBooker_IdOrderByStartDesc(Long bookerId);

    Collection<Booking> findBookingByBooker_IdAndStatusOrderByStartDesc(Long bookerId, Status status);

    Collection<Booking> findBookingByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    Collection<Booking> findBookingByBooker_IdAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

    Collection<Booking> findBookingByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(Long bookerId, LocalDateTime time, LocalDateTime time2);

    Collection<Booking> findBookingByItem_OwnerOrderByStartDesc(Long owner);

    Collection<Booking> findBookingByItem_OwnerAndStatusOrderByStartDesc(Long owner, Status status);

    Collection<Booking> findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(Long owner, LocalDateTime time);

    Collection<Booking> findBookingByItem_OwnerAndEndAfterOrderByStartDesc(Long owner, LocalDateTime time);

    Collection<Booking> findBookingByItem_OwnerAndEndAfterAndStartBeforeOrderByStartDesc(Long owner, LocalDateTime time, LocalDateTime time2);

    List<Booking> findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime time);

    List<Booking> findBookingByItem_IdAndStartIsAfterOrderByStart(Long itemId, LocalDateTime time);

    @Query(" Select b.item.id from Booking b where b.booker.id =?1 and b.status ='APPROVED' and b.start < ?2")
    Collection<Long> getBookedItemsIds(Long userId, LocalDateTime now);
}