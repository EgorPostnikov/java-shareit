package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;


@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "item.id",source = "itemId")
    @Mapping(target = "status", source = "status", defaultValue = "WAITING")
    Booking toBooking(BookingShort bookingShort);

    @Mapping(target = "status", source = "status", defaultValue = "WAITING")
    BookingDto toBookingDto(Booking booking);

    Collection<BookingDto> toBookingDtos(Collection<Booking> bookings);
}

