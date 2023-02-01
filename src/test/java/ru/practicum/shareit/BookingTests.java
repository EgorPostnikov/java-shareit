package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

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
        BookingServiceImpl bookingServiceImpl = new BookingServiceImpl();
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        bookingServiceImpl.setBookingRepository(mockBookingRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        bookingServiceImpl.setUserService(mockUserService);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        bookingServiceImpl.setItemService(mockItemService);

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
                .when(mockBookingRepository.save(Mockito.any()))
                .thenReturn(booking);

        BookingDto bookingDto = bookingServiceImpl.createBooking(3L, bookingShort);

        Assertions.assertEquals(2L, bookingDto.getItem().getOwner());

    }


}
