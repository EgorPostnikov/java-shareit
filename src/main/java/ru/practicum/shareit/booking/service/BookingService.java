package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InvalidAccessException;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingShort bookingShort) throws BadRequestException;

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) throws InvalidAccessException;

    BookingDto getBookingById(Long bookingId, Long userId) throws InvalidAccessException;

    Collection<BookingDto> getBookingsOfUser(Long userId, State state);

    Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state);
}
