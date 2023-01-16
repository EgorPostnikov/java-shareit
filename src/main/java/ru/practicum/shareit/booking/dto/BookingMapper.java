package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.Collection;


@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "itemId",source ="item")
    BookingDto toBookingDto(Booking booking);

    @Mapping(target = "item",source ="itemId")
    @Mapping(target = "status",source = "status", defaultValue = "WAITING")
    Booking toBooking(BookingDto bookingDto);

    Collection<BookingDto> toBookingDtos(Collection<Booking> bookings);
}

