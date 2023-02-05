package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserService mockUserService;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    CommentRepository mockCommentRepository;

    Long itemId = 1L;
    Long itemId2 = 2L;
    Long ownerId = 2L;
    Long bookerId = 2L;
    Item item = new Item(itemId,
            "Item name",
            "Item description",
            true,
            ownerId,
            null);
    Item item2 = new Item(itemId2,
            "Item name2",
            "Item description2",
            true,
            ownerId,
            null);
    ItemDto itemDto = new ItemDto(itemId,
            "Item name",
            "Item description",
            true,
            null,
            null,
            null);
    CommentDto comment = new CommentDto(
            1L,
            "Text",
            1L,
            1L,
            "name");
    Comment comment2 = new Comment(
            1L,
            "Text",
            1L,
            new User(1L, "Name", "mail@mail.ry"),
            LocalDateTime.now());
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

    ItemBookingDto itemBookingDto2 = new ItemBookingDto(1L, 1L);

    @Test
    void testCreateItemWithMockOk() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        ItemDto gotItemDto = itemServiceImpl.createItem(3L, itemDto);
        Assertions.assertEquals(1L, gotItemDto.getId());
    }

    @Test
    void testCreateItemWithMockWrongUser() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);

        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemServiceImpl.createItem(3L, itemDto));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }

    @Test
    void getItemById1() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);

        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemServiceImpl.getItemById(1L, 1L));

        Assertions.assertEquals("Item with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void getNextBooking() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Collections.emptyList());

        ItemBookingDto itemBookingDto = itemServiceImpl.getNextBooking(1L);
        Assertions.assertNull(itemBookingDto);
    }

    @Test
    void getNextBooking2() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings);

        ItemBookingDto itemBookingDto = itemServiceImpl.getNextBooking(1L);
        Assertions.assertEquals(booking.getId(), itemBookingDto.getId());
    }

    @Test
    void getLastBooking() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);

        itemServiceImpl.setBookingRepository(mockBookingRepository);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Mockito.eq(1L), Mockito.any()))
                .thenReturn(Collections.emptyList());

        ItemBookingDto itemBookingDto = itemServiceImpl.getLastBooking(1L);
        Assertions.assertNull(itemBookingDto);
    }

    @Test
    void getLastBooking2() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings);

        ItemBookingDto itemBookingDto = itemServiceImpl.getLastBooking(1L);
        Assertions.assertEquals(booking.getId(), itemBookingDto.getId());
    }

    @Test
    void findById() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));

        Item gotItem = itemServiceImpl.findById(1L);
        Assertions.assertEquals(item.getId(), gotItem.getId());
    }

    @Test
    void getOwnerOfItem() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));

        Long gotItem = itemServiceImpl.getOwnerOfItem(1L);
        Assertions.assertEquals(item.getOwner(), gotItem);
    }

    @Test
    void getCommentsByItemId() {
        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setCommentRepository(mockCommentRepository);

        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(comments);

        Collection<CommentDto> gotComments = itemServiceImpl.getCommentsByItemId(1L);
        Assertions.assertEquals(1, gotComments.size());
    }

    @Test
    void getItemById2() {

        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment2);

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setCommentRepository(mockCommentRepository);

        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));

        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(comments);

        ItemDtoWithComments gotItemDtoWithComments = itemServiceImpl.getItemById(1L, 3L);
        Assertions.assertEquals(1L, gotItemDtoWithComments.getId());
    }

    @Test
    void getItemById3() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking2);
        List<Booking> bookings2 = new ArrayList<>();
        bookings.add(booking2);
        bookings.add(booking);
        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment2);

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        itemServiceImpl.setCommentRepository(mockCommentRepository);

        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings2);

        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(comments);

        ItemDtoWithComments gotItemDtoWithComments = itemServiceImpl.getItemById(1L, 2L);
        Assertions.assertEquals(1L, gotItemDtoWithComments.getId());
    }

}

