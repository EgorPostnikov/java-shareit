package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingShort bookingShort);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsOfUser(Long userId, State state);

    Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state);
}
