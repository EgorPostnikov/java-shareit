package ru.practicum.server.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.model.BookedItem;
import ru.practicum.server.booking.model.Booker;

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

