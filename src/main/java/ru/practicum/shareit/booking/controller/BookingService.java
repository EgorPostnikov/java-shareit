package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.UnsupportedIdException;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsOfUser(Long userId, String state);

    List<BookingDto> getBookingsOfUsersItems(Long userId, String state);
}
