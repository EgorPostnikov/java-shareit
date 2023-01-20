package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingShort bookingShort);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsOfUser(Long userId, String state);

    Collection<BookingDto> getBookingsOfUsersItems(Long userId, String state);
}
