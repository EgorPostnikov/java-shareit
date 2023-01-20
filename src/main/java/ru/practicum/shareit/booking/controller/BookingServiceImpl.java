package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
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

public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapperImp bookingMapperImp;
    @Override
    public BookingDto createBooking(Long userId, BookingShort bookingShort)  {
        Item item = itemService.getItem(bookingShort.getItemId());
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

    Booking booking =  bookingRepository.getById(bookingId);
        System.out.println(booking.toString());
    BookedItem  bookedItem = booking.getItem();
    Long itemId=bookedItem.getId();
    Item item = itemService.getItem(itemId);
    if (item.getOwner()!=userId){
        throw new NoSuchElementException("  User id did not found");
    }
    if (approved) {
        booking.setStatus(Status.APPROVED);
    } else {
        booking.setStatus(Status.REJECTED);
    }
        Booking bookingGot =bookingRepository.save(booking);
    return BookingMapper.INSTANCE.toBookingDto(bookingGot);
    }
    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
       /*if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }*/
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemService.getItem(booking.getItem().getId());
        if ((booking.getBooker().getId()!=userId)&&(item.getOwner()!=userId)){
            throw new NoSuchElementException("  User id did not found");
        }
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(Long userId, String state) {
        Collection<Booking> bookings= bookingRepository.getBookingsOfUser(userId,state);
        return  BookingMapper.INSTANCE.toBookingDtos(bookings);
    }

    @Override
    public Collection<BookingDto> getBookingsOfUsersItems(Long userId, String state) {
        Collection<Booking> bookings= bookingRepository.getBookingsOfUsersItems(userId,state);
        return  BookingMapper.INSTANCE.toBookingDtos(bookings);
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
