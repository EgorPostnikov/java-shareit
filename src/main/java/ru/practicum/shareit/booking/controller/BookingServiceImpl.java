package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.UnsupportedIdException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor

public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto)  {
        if (!userService.isExistUser(userId)){
            throw new NoSuchElementException("  User id did not found");
        }
        if (!itemService.getItem(bookingDto.getItemId()).getAvailable()){
            throw new EntityNotFoundException("  Item not available for booking!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new EntityNotFoundException("  Booking end time is before start time!");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())
                ||(bookingDto.getStart().isBefore(LocalDateTime.now()))){
            throw new EntityNotFoundException("  Time in the past!");
        }

            Booking booking = BookingMapper.INSTANCE.toBooking(bookingDto);
        booking.setBooker(userId);

        if (itemService.getOwnerOfItem(bookingDto.getItemId())==userId){
            throw new EntityNotFoundException("Id's not correct!");
        }
        booking = bookingRepository.save(booking);
        System.out.println(booking.toString());
        System.out.println(BookingMapper.INSTANCE.toBookingDto(booking).toString());
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }


    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.getById(bookingId);
        if (itemService.getOwnerOfItem(bookingId)!=userId) {
            return null;
        }
          if(approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.INSTANCE.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getById(bookingId);
        if (booking.getBooker()!=userId ||itemService.getOwnerOfItem(bookingId)!=userId) {
            return null;
        }
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsOfUser(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsOfUsersItems(Long userId, String state) {
        return null;
    }
}
