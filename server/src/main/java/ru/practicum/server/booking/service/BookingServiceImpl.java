package ru.practicum.server.booking.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.mapper.BookingMapper;
import ru.practicum.server.booking.mapper.BookingMapperImp;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.exception.BadRequestException;
import ru.practicum.server.exception.InvalidAccessException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.item.service.ItemServiceImpl;
import ru.practicum.server.user.service.UserService;
import ru.practicum.server.booking.model.BookedItem;
import ru.practicum.server.booking.model.State;
import ru.practicum.server.booking.storage.BookingRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@Setter
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    private BookingRepository bookingRepository;
    private UserService userService;
    private ItemService itemService;
    private BookingMapperImp bookingMapperImp;

    @Override
    public BookingDto createBooking(Long userId, BookingShort bookingShort) throws BadRequestException {
        Item item = itemService.getItem(bookingShort.getItemId());

        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User id #" + userId + " did not found!");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item id#" + item.getId() + " not available for booking!");
        }
        if (bookingShort.getEnd().isBefore(bookingShort.getStart())) {
            throw new BadRequestException("Booking end time is before start time!");
        }
        if (bookingShort.getEnd().isBefore(LocalDateTime.now())
                || (bookingShort.getStart().isBefore(LocalDateTime.now()))) {
            throw new BadRequestException("Time in the past!");
        }
        if (itemService.getOwnerOfItem(bookingShort.getItemId()).equals(userId)) {
            throw new NoSuchElementException("Id's not correct!");
        }
        Booking booking = bookingMapperImp.toBooking(bookingShort);
        booking.getBooker().setId(userId);
        booking.getItem().setName(item.getName());
        booking = bookingRepository.save(booking);
        log.info("Booking with id #{} saved", booking.getId());
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) throws InvalidAccessException {

        Booking booking = bookingRepository.findById(bookingId).get();
        ;
        BookedItem bookedItem = booking.getItem();
        Long itemId = bookedItem.getId();
        Item item = itemService.getItem(itemId);
        if (booking.getStatus() != Status.WAITING) {
            throw new EntityNotFoundException("Status is already set");
        }
        if (!item.getOwner().equals(userId)) {
            throw new InvalidAccessException("User " + userId + " have not roots!");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking bookingGot = bookingRepository.save(booking);
        log.info("Booking with id #{} updated", bookingGot.getId());
        return BookingMapper.INSTANCE.toBookingDto(bookingGot);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) throws InvalidAccessException {
        if (!isExistBooking(bookingId)) {
            throw new NoSuchElementException("Booking with id #" + bookingId + " didn't found!");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemService.getItem(booking.getItem().getId());
        if ((!booking.getBooker().getId().equals(userId)) && (!item.getOwner().equals(userId))) {
            throw new InvalidAccessException("User id#" + userId + " have not roots!");
        }
        log.info("Booking with id #{} found", booking.getId());
        return BookingMapper.INSTANCE.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsOfUser(Long userId, State state, PageRequest pageRequest) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        Collection<Booking> bookings = null;
        switch (state) {
            case WAITING:
                bookings = bookingRepository
                        .findBookingByBooker_IdAndStatusOrderByStartDesc(userId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findBookingByBooker_IdAndStatusOrderByStartDesc(userId, Status.REJECTED, pageRequest);
                break;
            case ALL:
                bookings = bookingRepository
                        .findBookingByBooker_IdOrderByStartDesc(userId, pageRequest);
                break;
            case PAST:
                bookings = bookingRepository
                        .findBookingByBooker_IdAndEndBeforeOrderByStartDesc(userId, currentTime, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findBookingByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                                currentTime, currentTime, pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findBookingByBooker_IdAndEndAfterOrderByStartDesc(userId, currentTime, pageRequest);
                break;
        }
        log.info("Bookings list of user got, bookings qty is #{}", bookings.size());
        return BookingMapper.INSTANCE.toBookingDtos(bookings);
    }

    @Override
    public Collection<BookingDto> getBookingsOfUsersItems(Long userId, State state, PageRequest pageRequest) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        Collection<Booking> bookings = null;
        switch (state) {
            case WAITING:
                bookings = bookingRepository
                        .findBookingByItem_OwnerAndStatusOrderByStartDesc(userId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findBookingByItem_OwnerAndStatusOrderByStartDesc(userId, Status.REJECTED, pageRequest);
                break;
            case ALL:
                bookings = bookingRepository
                        .findBookingByItem_OwnerOrderByStartDesc(userId, pageRequest);
                break;
            case PAST:
                bookings = bookingRepository
                        .findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(userId, currentTime, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findBookingByItem_OwnerAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                                currentTime, currentTime, pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findBookingByItem_OwnerAndEndAfterOrderByStartDesc(userId, currentTime, pageRequest);
                break;
        }
        log.info("Bookings list of users items got, bookings qty are #{}", bookings.size());
        return BookingMapper.INSTANCE.toBookingDtos(bookings);
    }

    public boolean isExistBooking(Long bookingId) {
        return bookingRepository.existsById(bookingId);
    }

}
