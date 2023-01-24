package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.State;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingMapperImp;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapperImp bookingMapperImp;

    @Override
    public BookingDto createBooking(Long userId, BookingShort bookingShort) {
        Item item = itemService.getItem(bookingShort.getItemId());

        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("  User id did not found");
        }

        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("  User id did not found");
        }
        if (!item.getAvailable()) {
            throw new EntityNotFoundException("  Item not available for booking!");
        }
        if (bookingShort.getEnd().isBefore(bookingShort.getStart())) {
            throw new EntityNotFoundException("  Booking end time is before start time!");
        }
        if (bookingShort.getEnd().isBefore(LocalDateTime.now())
                || (bookingShort.getStart().isBefore(LocalDateTime.now()))) {
            throw new EntityNotFoundException("  Time in the past!");
        }

        if (itemService.getOwnerOfItem(bookingShort.getItemId()) == userId) {
            throw new NoSuchElementException("Id's not correct!");
        }
        Booking booking = bookingMapperImp.toBooking(bookingShort);
        booking.getBooker().setId(userId);
        booking.getItem().setName(item.getName());
        Booking booking2 = bookingRepository.save(booking);
        BookingDto bookingDto1 = BookingMapper.INSTANCE.toBookingDto(booking2);

        return bookingDto1;
    }

    @Override
    @Transactional
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {

        Booking booking = bookingRepository.getById(bookingId);
        BookedItem bookedItem = booking.getItem();
        Long itemId = bookedItem.getId();
        Item item = itemService.getItem(itemId);
        if (booking.getStatus() != Status.WAITING) {
            throw new EntityNotFoundException("Status is already set");
        }
        if (item.getOwner() != userId) {
            throw new NoSuchElementException("User id did not found");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking bookingGot = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.toBookingDto(bookingGot);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (!isExistBooking(bookingId)) {
            throw new NoSuchElementException("Booking with id #" + bookingId + " didn't found!");
        }
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemService.getItem(booking.getItem().getId());
        if ((booking.getBooker().getId() != userId) && (item.getOwner() != userId)) {
            throw new NoSuchElementException("  User id did not found");
        }
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(Long userId, State state) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        Collection<Booking> bookings = null;
        switch (state) {
            case WAITING:
                bookings = bookingRepository.findBookingByBooker_IdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingByBooker_IdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findBookingByBooker_IdOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = bookingRepository.findBookingByBooker_IdAndEndBeforeOrderByStartDesc(userId, currentTime);
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId, currentTime, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingByBooker_IdAndEndAfterOrderByStartDesc(userId, currentTime);
                break;
        }
        return BookingMapper.INSTANCE.toBookingDtos(bookings);
    }


    @Override
    public Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        Collection<Booking> bookings = null;
        switch (state) {
            case WAITING:
                bookings = bookingRepository.findBookingByItem_OwnerAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingByItem_OwnerAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findBookingByItem_OwnerOrderByStartDesc(userId);
                break;
            case PAST:
                bookings = bookingRepository.findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(userId, currentTime);
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingByItem_OwnerAndEndAfterAndStartBeforeOrderByStartDesc(userId, currentTime, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingByItem_OwnerAndEndAfterOrderByStartDesc(userId, currentTime);
                break;
        }
        return BookingMapper.INSTANCE.toBookingDtos(bookings);
    }

    public boolean isExistBooking(Long bookingId) {
        return bookingRepository.existsById(bookingId);
    }
}

        /*Item item = itemService.getItem(bookingShort.getItemId());

        if (!userService.isExistUser(userId)){
            throw new NoSuchElementException("  User id did not found");
        }
        if (!item.getAvailable()){
            throw new EntityNotFoundException("  Item not available for booking!");
        }
        if (bookingShort.getEnd().isBefore(bookingShort.getStart())) {
            throw new EntityNotFoundException("  Booking end time is before start time!");
        }
        if (bookingShort.getEnd().isBefore(LocalDateTime.now())
                ||(bookingShort.getStart().isBefore(LocalDateTime.now()))){
            throw new EntityNotFoundException("  Time in the past!");
        }

        if (itemService.getOwnerOfItem(bookingShort.getItemId())==userId){
            throw new EntityNotFoundException("Id's not correct!");
        }*/
