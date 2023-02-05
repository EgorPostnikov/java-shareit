package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
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

    Collection<BookingDto> getBookingsOfUser(Long userId, State state, PageRequest pageRequest);

    Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state, PageRequest pageRequest);
}
/*Warning:  Rule violated for bundle shareit: lines covered ratio is 0.4, but expected minimum is 0.9
        Warning:  Rule violated for bundle shareit: branches covered ratio is 0.2, but expected minimum is 0.6
        Warning:  Rule violated for bundle shareit: complexity covered ratio is 0.4, but expected minimum is 0.6
        Warning:  Rule violated for bundle shareit: methods covered ratio is 0.6, but expected minimum is 0.7*/