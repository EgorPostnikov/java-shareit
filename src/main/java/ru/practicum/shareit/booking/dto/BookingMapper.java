package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;


import java.util.Collection;


@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    /*@Mapping(target = "status",source = "status", defaultValue = "WAITING")
    Booking toBooking(BookingShort bookingShort);*/

    @Mapping(target = "status",source = "status", defaultValue = "WAITING")
    BookingDto toBookingDto(Booking booking);
    Collection<BookingDto> toBookingDtos(Collection<Booking> bookings);
}

