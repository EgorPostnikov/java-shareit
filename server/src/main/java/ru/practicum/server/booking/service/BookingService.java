package ru.practicum.server.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.exception.BadRequestException;
import ru.practicum.server.exception.InvalidAccessException;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.model.State;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingShort bookingShort) throws BadRequestException;

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) throws InvalidAccessException;

    BookingDto getBookingById(Long bookingId, Long userId) throws InvalidAccessException;

    Collection<BookingDto> getBookingsOfUser(Long userId, State state, PageRequest pageRequest);

    Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state, PageRequest pageRequest);
}
