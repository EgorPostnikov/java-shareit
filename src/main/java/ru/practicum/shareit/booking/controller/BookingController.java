package ru.practicum.shareit.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.validation.Create;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }
    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }
    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(itemId,userId);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsOfUser(userId, state);
    }
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsOfUsersItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsOfUsersItems(userId, state);
    }
}
