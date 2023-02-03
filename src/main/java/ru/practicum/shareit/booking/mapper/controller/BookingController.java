package ru.practicum.shareit.booking.mapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InvalidAccessException;
import ru.practicum.shareit.response.Response;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingShort bookingShort) throws BadRequestException {
        return bookingService.createBooking(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam Boolean approved) throws InvalidAccessException {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@PathVariable long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) throws InvalidAccessException {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") State state,
                                                    @RequestParam(defaultValue = "1") Integer from,
                                                    @RequestParam(defaultValue = "100") Integer size) {
        PageRequest pageRequest = PageRequest.of(from - 1, size, Sort.unsorted());
        return bookingService.getBookingsOfUser(userId, state, pageRequest);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsOfUsersItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "ALL") State state,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "100") Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.unsorted());
        return bookingService.getBookingsOfUsersItems(userId, state, pageRequest);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Response handleException(EntityNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler()
    public Map<String, String> handleConversionException(final ConversionFailedException e) {
        return Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidAccessException.class)
    public Response handleException(InvalidAccessException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Response handleException(BadRequestException exception) {
        return new Response(exception.getMessage());
    }

}
