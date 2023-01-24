package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

@Component
public class BookingMapperImp {
    public Booking toBooking(BookingShort bookingShort) {
        return new Booking(
                bookingShort.getId(),
                bookingShort.getStart(),
                bookingShort.getEnd(),
                new BookedItem(bookingShort.getItemId(), null, null),
                new Booker(),
                Status.WAITING
        );
    }
}

