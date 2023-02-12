package ru.practicum.shareit.booking.repository.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingMapperImp;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InvalidAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    UserService mockUserService;
    @Mock
    BookingMapperImp mockBookingMapperImp;
    @Mock
    ItemService mockItemService;

    Long itemId = 1L;
    Long ownerId = 1L;
    Long bookerId = 2L;
    Item item = new Item(
            itemId,
            "Item name",
            "Item description",
            true,
            ownerId,
            null);
    BookingShort bookingShort = new BookingShort(
            null,
            itemId,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(2));
    Booking booking = new Booking(1L,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(2),
            new BookedItem(item.getId(), item.getName(), item.getOwner()),
            new Booker(bookerId),
            Status.WAITING);
    Booking booking2 = new Booking(2L,
            LocalDateTime.now().plusMinutes(3),
            LocalDateTime.now().plusMinutes(4),
            new BookedItem(item.getId(), item.getName(), item.getOwner()),
            new Booker(bookerId),
            Status.REJECTED);

    @Test
    void updateBookingWithMock1() throws InvalidAccessException {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);
        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingDto bookingDto = bookingServiceImpl.updateBooking(1L, 1L, true);
        Assertions.assertEquals(1L, bookingDto.getId());
        Assertions.assertEquals(Status.APPROVED, bookingDto.getStatus());
    }

    @Test
    void updateBookingWithMock2() throws InvalidAccessException {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);
        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingDto bookingDto = bookingServiceImpl.updateBooking(1L, 1L, false);
        Assertions.assertEquals(1L, bookingDto.getId());
        Assertions.assertEquals(Status.REJECTED, bookingDto.getStatus());
    }

    @Test
    void updateBookingWithMock3() {
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking2));
        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookingServiceImpl.updateBooking(1L, 1L, true));

        Assertions.assertEquals("Status is already set", exception.getMessage());
    }

    @Test
    void updateBookingWithMock4() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        final InvalidAccessException exception = Assertions.assertThrows(
                InvalidAccessException.class,
                () -> bookingServiceImpl.updateBooking(1L, 3L, true));

        Assertions.assertEquals("User 3 have not roots!", exception.getMessage());
    }

    @Test
    void getBookingByIdWithMock1() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);
        Mockito
                .when(mockBookingRepository.existsById(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingServiceImpl.getBookingById(1L, 1L));

        Assertions.assertEquals("Booking with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void getBookingByIdWithMock2() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);
        Mockito
                .when(mockBookingRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(mockItemService.getItem(Mockito.eq(1L)))
                .thenReturn(item);

        final InvalidAccessException exception = Assertions.assertThrows(
                InvalidAccessException.class,
                () -> bookingServiceImpl.getBookingById(1L, 3L));

        Assertions.assertEquals("User id#3 have not roots!", exception.getMessage());
    }

    @Test
    void getBookingByIdWithMock3() throws InvalidAccessException {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);
        Mockito
                .when(mockBookingRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(mockItemService.getItem(Mockito.eq(1L)))
                .thenReturn(item);

        BookingDto bookingDto = bookingServiceImpl.getBookingById(1L, 1L);
        Assertions.assertEquals(1L, bookingDto.getId());
        Assertions.assertEquals(Status.WAITING, bookingDto.getStatus());
    }

    @Test
    void isExistBooking() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        Mockito
                .when(mockBookingRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Boolean bookingDto = bookingServiceImpl.isExistBooking(1L);
        Assertions.assertEquals(true, bookingDto);
    }

    @Test
    void getBookingsOfUser1() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(false);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingServiceImpl.getBookingsOfUser(1L, State.WAITING, pageRequest));

        Assertions.assertEquals("User with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void getBookingsOfUser2() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdAndStatusOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.eq(Status.WAITING),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.WAITING, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUser3() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdAndStatusOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.eq(Status.REJECTED),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.REJECTED, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUser4() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.ALL, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUser5() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdAndEndBeforeOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.PAST, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUser6() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.CURRENT, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUser7() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByBooker_IdAndEndAfterOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUser(1L, State.FUTURE, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems1() {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(false);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingServiceImpl.getBookingsOfUsersItems(1L, State.WAITING, pageRequest));

        Assertions.assertEquals("User with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void getBookingsOfUsersItems2() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerAndStatusOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.eq(Status.WAITING),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.WAITING, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems3() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerAndStatusOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.eq(Status.REJECTED),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.REJECTED, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems4() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.ALL, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems5() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.PAST, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems6() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerAndEndAfterAndStartBeforeOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.CURRENT, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void getBookingsOfUsersItems7() {
        Collection<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository
                        .findBookingByItem_OwnerAndEndAfterOrderByStartDesc(
                                Mockito.eq(1L),
                                Mockito.any(),
                                Mockito.any()))
                .thenReturn(bookings);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<BookingDto> gotBookings = bookingServiceImpl.getBookingsOfUsersItems(1L, State.FUTURE, pageRequest);
        Assertions.assertEquals(2, gotBookings.size());
    }

    @Test
    void testCreateBookingWithMockOk() throws BadRequestException {

        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);
        bookingServiceImpl.setBookingMapperImp(mockBookingMapperImp);

        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);
        Mockito
                .when(mockItemService.getOwnerOfItem(Mockito.anyLong()))
                .thenReturn(ownerId);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        Mockito
                .when(mockBookingMapperImp.toBooking(Mockito.any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingServiceImpl.createBooking(3L, bookingShort);

        Assertions.assertEquals(1L, bookingDto.getId());
    }

    @Test
    void testCreateBookingWithMockWrongUser() {
        Long itemId = 1L;
        BookingShort bookingShort = new BookingShort(null, itemId, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);
        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("User id #3 did not found!", exception.getMessage());
    }

    /*@Test
    void testCreateBookingWithMockWrongStartTime() {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId, "Item name", "Item description", true, ownerId, null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null, itemId, LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(2));
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(),
                bookedItem, booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Time in the past!", exception.getMessage());
    }*/

   /* @Test
    void testCreateBookingWithMockWrongEndTime() {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId, "Item name", "Item description", true, ownerId, null);
        BookingShort bookingShort = new BookingShort(null, itemId, LocalDateTime.now().plusMinutes(1), LocalDateTime.now());
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Booking end time is before start time!", exception.getMessage());
    }*/

    @Test
    void testCreateBookingWithMockWrongItemAvailability() {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId, "Item name", "Item description", false, ownerId, null);
        BookingShort bookingShort = new BookingShort(null, itemId, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2));
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null, null, null);
        bookingServiceImpl.setUserService(mockUserService);
        bookingServiceImpl.setItemService(mockItemService);

        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);

        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Item id#1 not available for booking!", exception.getMessage());
    }
}
