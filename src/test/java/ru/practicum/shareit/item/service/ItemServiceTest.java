package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookedItem;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidAccessException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
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
            1L);
    Item item2 = new Item(itemId2,
            "Item name2",
            "Item description2",
            true,
            ownerId,
            1L);
    ItemDto itemDto = new ItemDto(itemId,
            "ItemDto name",
            "ItemDto description",
            false,
            1L,
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
    void getCommentsByItemId2() {
        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setCommentRepository(mockCommentRepository);
        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(null);

        Collection<CommentDto> foundItems = itemServiceImpl.getCommentsByItemId(1L);
        Assertions.assertNull(foundItems);
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
                .when(mockBookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(Mockito.eq(1L), Mockito.any()))
                .thenReturn(bookings2);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(comments);

        ItemDtoWithComments gotItemDtoWithComments = itemServiceImpl.getItemById(1L, 2L);
        Assertions.assertEquals(1L, gotItemDtoWithComments.getId());
    }

    @Test
    void getItem() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);

        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));

        Item itemGot = itemServiceImpl.getItem(1L);
        Assertions.assertEquals(1L, itemGot.getId());
    }

    @Test
    void getItem2() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);

        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemServiceImpl.getItem(1L));

        Assertions.assertEquals("Item with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void updateItem() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);

        Mockito
                .when(mockItemRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(item));

        final InvalidAccessException exception = Assertions.assertThrows(
                InvalidAccessException.class,
                () -> itemServiceImpl.updateItem(1L, itemDto));

        Assertions.assertEquals("Access rights are not defined", exception.getMessage());
    }

    @Test
    void updateItem2() throws InvalidAccessException {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(mockUserService.getUserById(Mockito.anyLong()))
                .thenReturn(new UserDto(1L, "name", "mail3@email.ru"));

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        ItemDto gotItemDto = itemServiceImpl.updateItem(2L, itemDto);
        Assertions.assertEquals(itemDto.getId(), gotItemDto.getId());
        Assertions.assertEquals(itemDto.getRequestId(), gotItemDto.getRequestId());
        Assertions.assertEquals(itemDto.getDescription(), gotItemDto.getDescription());
        Assertions.assertEquals(itemDto.getName(), gotItemDto.getName());
    }

    @Test
    void getItems() {
        List<Booking> bookings = new ArrayList<>();
        Collection<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        bookings.add(booking);
        bookings.add(booking2);
        List<Booking> bookings2 = new ArrayList<>();
        bookings.add(booking2);
        bookings.add(booking);
        Collection<Comment> comments = new ArrayList<>();
        comments.add(comment2);

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        itemServiceImpl.setCommentRepository(mockCommentRepository);

        Mockito
                .when(mockItemRepository.getItemsByOwnerOrderById(Mockito.anyLong(), Mockito.any()))
                .thenReturn(items);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(bookings);
        Mockito
                .when(mockBookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(Mockito.anyLong(), Mockito.any()))
                .thenReturn(bookings2);
        Mockito
                .when(mockCommentRepository.getCommentsByItemEquals(Mockito.eq(1L)))
                .thenReturn(comments);


        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<ItemDtoWithComments> gotItemDtoWithComments = itemServiceImpl.getItems(2L, pageRequest);
        Assertions.assertEquals(2, gotItemDtoWithComments.size());
    }

    @Test
    void getItems2() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserService(mockUserService);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        itemServiceImpl.setCommentRepository(mockCommentRepository);

        Mockito
                .when(mockItemRepository.getItemsByOwnerOrderById(Mockito.eq(1L), Mockito.any()))
                .thenReturn(null);

        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<ItemDtoWithComments> gotItemDtoWithComments = itemServiceImpl.getItems(1L, pageRequest);
        Assertions.assertNull(gotItemDtoWithComments);
    }

    @Test
    void searchItems() {
        Collection<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        Mockito
                .when(mockItemRepository.searchItems(Mockito.eq("text")))
                .thenReturn(items);

        Collection<ItemDto> foundItems = itemServiceImpl.searchItems("TEXT");
        Assertions.assertEquals(2, foundItems.size());
    }

    @Test
    void searchItems2() {

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);

        Collection<ItemDto> foundItems = itemServiceImpl.searchItems("");
        Assertions.assertEquals(0, foundItems.size());
    }

    @Test
    void isExistItem() {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setItemRepository(mockItemRepository);
        Mockito
                .when(mockItemRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Boolean foundItems = itemServiceImpl.isExistItem(1L);
        Assertions.assertEquals(true, foundItems);
    }

    @Test
    void createComment() {
        Collection<Comment> comments = new ArrayList<>();
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        comments.add(comment2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setBookingRepository(mockBookingRepository);
        itemServiceImpl.setCommentRepository(mockCommentRepository);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockBookingRepository.getBookedItemsIds(Mockito.eq(comment.getItem()), Mockito.any()))
                .thenReturn(ids);
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Mockito
                .when(mockUserService.getUserById(Mockito.anyLong()))
                .thenReturn(new UserDto(1L, "name", "mail@mail.ry"));

        CommentDto gotComment = itemServiceImpl.createComment(comment);
        Assertions.assertEquals(comment.getAuthorId(), gotComment.getAuthorId());
        Assertions.assertEquals(comment.getText(), gotComment.getText());
        Assertions.assertEquals(comment.getAuthorName(), gotComment.getAuthorName());
        Assertions.assertEquals(comment.getItem(), gotComment.getItem());
    }

    @Test
    void createComment2() {
        Collection<Comment> comments = new ArrayList<>();
        Collection<Long> ids = new ArrayList<>();
        comments.add(comment2);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);
        itemServiceImpl.setBookingRepository(mockBookingRepository);

        Mockito
                .when(mockBookingRepository.getBookedItemsIds(Mockito.eq(comment.getItem()), Mockito.any()))
                .thenReturn(ids);

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemServiceImpl.createComment(comment));

        Assertions.assertEquals("Item was not booked by author of comment", exception.getMessage());
    }

    @Test
    void createComment3() {
        CommentDto wrongComment = comment;
        wrongComment.setText("   ");
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null, null);

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemServiceImpl.createComment(wrongComment));

        Assertions.assertEquals("Text is empty", exception.getMessage());
    }
}

