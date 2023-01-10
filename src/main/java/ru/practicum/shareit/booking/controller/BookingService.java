package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@AllArgsConstructor

public class BookingService {
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        return null;
    }

    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        return null;
    }

    public BookingDto getBookingById(Long itemId, Long userId) {
        return null;
    }

    public List<BookingDto> getBookingsOfUser(Long userId, String state) {
        return null;
    }

    public List<BookingDto> getBookingsOfUsersItems(Long userId, String state) {
        return null;
    }
}
