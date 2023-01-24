package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemRepository.save(ItemMapper.INSTANCE.toItem(itemDto, userId));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDtoWithComments getItemById(long itemId, Long userId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        Item item = itemRepository.getById(itemId);
        ItemDtoWithComments itemDto = ItemMapper.INSTANCE.toItemDtoWithComments(item);
        Boolean isOwner = (item.getOwner() == userId);
        if (isOwner) {
            itemDto.setNextBooking(getNextBooking(itemId));
            itemDto.setLastBooking(getLastBooking(itemId));
        }
        itemDto.setComments(getCommentsByItemId(itemId));
        return itemDto;
    }

    @Override
    public Item getItem(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        return itemRepository.getById(itemId);
    }

    @Override
    public Long getOwnerOfItem(long itemId) {
        return itemRepository.getById(itemId).getOwner();
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.INSTANCE.toItem(itemDto, userId);
        Item updatedItem = itemRepository.getById(itemDto.getId());
        if (!(updatedItem.getOwner() == userId)) {
            throw new NoSuchElementException("Access rights are not defined");
        }
        userService.getUserById(userId);
        if (item.getName() == null) {
            item.setName(updatedItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        item.setOwner(updatedItem.getOwner());
        item.setRequest(updatedItem.getRequest());

        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemDtoWithComments> getItems(long userId) {
        Collection<Item> items = itemRepository.getItems(userId);
        if (items == null) {
            return null;
        } else {
            Collection<ItemDtoWithComments> collection = new ArrayList(items.size());
            Iterator var3 = items.iterator();
            while (var3.hasNext()) {
                Item item = (Item) var3.next();
                Long itemId = item.getId();
                Boolean isOwner = (item.getOwner() == userId);
                ItemDtoWithComments itemDto = ItemMapper.INSTANCE.toItemDtoWithComments(item);
                if (isOwner) {
                    itemDto.setNextBooking(getNextBooking(itemId));
                    itemDto.setLastBooking(getLastBooking(itemId));
                }
                itemDto.setComments(getCommentsByItemId(itemId));
                collection.add(itemDto);
            }
            return collection;
        }
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        text = text.toLowerCase();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return ItemMapper.INSTANCE.toItemDtos(itemRepository.searchItems(text));
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return itemRepository.existsById(itemId);
    }


    public ItemBookingDto getLastBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return null;
        } else {
            Booking booking = bookings.get(0);
            return new ItemBookingDto(booking.getId(), booking.getBooker().getId());
        }
    }

    public ItemBookingDto getNextBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return null;
        } else {
            Booking booking = bookings.get(0);
            return new ItemBookingDto(booking.getId(), booking.getBooker().getId());
        }
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Long authorId = commentDto.getAuthorId();
        if (commentDto.getText().isBlank()) {
            throw new EntityNotFoundException("Text is empty");
        }
        if (!bookingRepository.getBookedItemsIds(authorId, LocalDateTime.now()).contains(commentDto.getItem())) {
            throw new EntityNotFoundException("Item was not booked by author of comment");
        }
        Comment comment = CommentMapper.INSTANCE.toComment(commentDto);
        comment = commentRepository.save(comment);
        comment.getAuthor().setName(userService.getUserById(authorId).getName());
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    public Collection<CommentDto> getCommentsByItemId(Long itemId) {
        return CommentMapper.INSTANCE.toCommentDtos(commentRepository.getCommentsByItemEquals(itemId));
    }
}
