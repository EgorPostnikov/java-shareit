package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.Collection;


@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto toBookingDto(Booking booking);

    //@Mapping( target = "status", defaultExpression = Status.WAITING)
    Booking toBooking(BookingDto bookingDto,Long item, Long booker, Status waiting);

    Collection<BookingDto> toBookingDtos(Collection<Booking> bookings);
}

