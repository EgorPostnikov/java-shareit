package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingMapperImp;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@SpringBootTest
public class BookingTests {
    @Test
    void testCreateBookingWithMockOk() throws BadRequestException {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId,"Item name","Item description",true,ownerId,null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null,itemId,LocalDateTime.now().plusMinutes(1),LocalDateTime.now().plusMinutes(2));
        Booking booking = new Booking(1L,LocalDateTime.now(),LocalDateTime.now(),
                bookedItem,booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null,null,null);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);
        BookingMapperImp mockBookingMapperImp = Mockito.mock(BookingMapperImp.class);
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
    void testCreateBookingWithMockWrongUser()  {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId,"Item name","Item description",true,ownerId,null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null,itemId,LocalDateTime.now().plusMinutes(1),LocalDateTime.now().plusMinutes(2));
        Booking booking = new Booking(1L,LocalDateTime.now(),LocalDateTime.now(),
                bookedItem,booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null,null,null);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);
        BookingMapperImp mockBookingMapperImp = Mockito.mock(BookingMapperImp.class);
        bookingServiceImpl.setBookingMapperImp(mockBookingMapperImp);

        Mockito
                .when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(item);
        Mockito
                .when(mockItemService.getOwnerOfItem(Mockito.anyLong()))
                .thenReturn(ownerId);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);
        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        Mockito
                .when(mockBookingMapperImp.toBooking(Mockito.any()))
                .thenReturn(booking);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }
    @Test
    void testCreateBookingWithMockWrongStartTime()  {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId,"Item name","Item description",true,ownerId,null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null,itemId,LocalDateTime.now().minusMinutes(1),LocalDateTime.now().plusMinutes(2));
        Booking booking = new Booking(1L,LocalDateTime.now(),LocalDateTime.now(),
                bookedItem,booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null,null,null);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);
        BookingMapperImp mockBookingMapperImp = Mockito.mock(BookingMapperImp.class);
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

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Time in the past!", exception.getMessage());
    }
    @Test
    void testCreateBookingWithMockWrongEndTime()  {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId,"Item name","Item description",true,ownerId,null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null,itemId,LocalDateTime.now().plusMinutes(1),LocalDateTime.now());
        Booking booking = new Booking(1L,LocalDateTime.now(),LocalDateTime.now(),
                bookedItem,booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null,null,null);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);
        BookingMapperImp mockBookingMapperImp = Mockito.mock(BookingMapperImp.class);
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

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Booking end time is before start time!", exception.getMessage());
    }
    @Test
    void testCreateBookingWithMockWrongItemAvailability()  {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId,"Item name","Item description",false,ownerId,null);
        BookedItem bookedItem = new BookedItem();
        Booker booker = new Booker();
        BookingShort bookingShort = new BookingShort(null,itemId,LocalDateTime.now().plusMinutes(1),LocalDateTime.now().plusMinutes(2));
        Booking booking = new Booking(1L,LocalDateTime.now(),LocalDateTime.now(),
                bookedItem,booker, Status.WAITING);
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl(null, null,null,null);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);
        BookingMapperImp mockBookingMapperImp = Mockito.mock(BookingMapperImp.class);
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

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> bookingServiceImpl.createBooking(3L, bookingShort));

        Assertions.assertEquals("Item not available for booking!", exception.getMessage());
    }

}
