package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.dto.BookingShort;
import ru.practicum.shareit.gateway.booking.dto.State;
import ru.practicum.shareit.gateway.exception.BadRequestException;
import ru.practicum.shareit.gateway.exception.InvalidAccessException;
import ru.practicum.shareit.gateway.response.Response;
import ru.practicum.shareit.gateway.validation.Create;
import ru.practicum.shareit.gateway.validation.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
//@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated(Create.class) @RequestBody BookingShort bookingShort) throws EntityNotFoundException {
        if (bookingShort.getEnd().isBefore(bookingShort.getStart())) {
            throw new EntityNotFoundException("Booking end time is before start time!");
        }
        log.info("Create booking of userId={}", userId);
        return bookingClient.createBooking(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam String approved) {
        log.info("Change booking approving to -{}- of User with userId={}", approved, userId);
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get booking with bookingId={}, by userId={}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingsOfUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @RequestParam(name = "from", defaultValue = "1") Integer from,
            @RequestParam(name = "size", defaultValue = "100") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking of User with state {}, userId={}, from={}, size={}",
                stateParam, userId, from, size);
        return bookingClient.getBookingsOfUser(userId, state, from - 1, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingsOfUsersItems(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "100") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking of User's items with state {}, userId={}, from={}, size={}",
                stateParam, userId, from, size);
        return bookingClient.getBookingsOfUsersItems(userId, state, from, size);
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
    @ExceptionHandler(IllegalArgumentException.class)
    public Error handleConversionException(final IllegalArgumentException e) {
        return new Error("Unknown state: UNSUPPORTED_STATUS");
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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidationException.class)
    public Response handleException(ValidationException exception) {
        return new Response(exception.getMessage());
    }

}
