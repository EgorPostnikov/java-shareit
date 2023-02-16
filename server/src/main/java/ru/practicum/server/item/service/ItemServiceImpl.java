package ru.practicum.server.item.service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.InvalidAccessException;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemBookingDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithComments;
import ru.practicum.server.item.mapper.CommentMapper;
import ru.practicum.server.item.mapper.ItemMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.storage.CommentRepository;
import ru.practicum.server.item.storage.ItemRepository;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.storage.BookingRepository;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Setter
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    private UserService userService;
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User id # " + userId + " did not found!");
        }
        Item item = itemRepository.save(ItemMapper.INSTANCE.toItem(itemDto, userId));
        log.info("Item with id #{} saved", item.getId());
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDtoWithComments getItemById(long itemId, Long userId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        Item item = findById(itemId);
        ItemDtoWithComments itemDto = ItemMapper.INSTANCE.toItemDtoWithComments(item);
        boolean isOwner = (item.getOwner().equals(userId));
        if (isOwner) {
            itemDto.setNextBooking(getNextBooking(itemId));
            itemDto.setLastBooking(getLastBooking(itemId));
        }
        itemDto.setComments(getCommentsByItemId(itemId));
        log.info("Item with id #{} found", itemId);
        return itemDto;
    }

    @Override
    public Item getItem(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        log.info("Item with id #{} found", itemId);
        return findById(itemId);
    }

    @Override
    public Long getOwnerOfItem(long itemId) {
        return findById(itemId).getOwner();
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) throws InvalidAccessException {
        Item item = ItemMapper.INSTANCE.toItem(itemDto, userId);
        Item updatedItem = findById(itemDto.getId());
        if (!(updatedItem.getOwner() == userId)) {
            throw new InvalidAccessException("Access rights are not defined");
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
        item.setRequestId(updatedItem.getRequestId());
        log.info("Item with id #{} updated", item.getId());
        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemDtoWithComments> getItems(long userId, PageRequest pageRequest) {
        Collection<Item> items = itemRepository.getItemsByOwnerOrderById(userId, pageRequest);
        if (items == null) {
            return null;
        } else {
            Collection<ItemDtoWithComments> collection = new ArrayList(items.size());
            Iterator var3 = items.iterator();
            while (var3.hasNext()) {
                Item item = (Item) var3.next();
                Long itemId = item.getId();
                boolean isOwner = (item.getOwner() == userId);
                ItemDtoWithComments itemDto = ItemMapper.INSTANCE.toItemDtoWithComments(item);
                if (isOwner) {
                    itemDto.setNextBooking(getNextBooking(itemId));
                    itemDto.setLastBooking(getLastBooking(itemId));
                }
                itemDto.setComments(getCommentsByItemId(itemId));
                collection.add(itemDto);
            }
            log.info("Users list found, users quantity is #{}", collection.size());
            return collection;
        }
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        text = text.toLowerCase();
        log.info("List of items, containing text -{}- got", text);
        return ItemMapper.INSTANCE.toItemDtos(itemRepository.searchItems(text));
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    public ItemBookingDto getLastBooking(Long itemId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findBookingByItem_IdAndEndIsBeforeOrderByEndDesc(itemId, currentTime);
        if (bookings.isEmpty()) {
            return null;
        } else {
            Booking booking = bookings.get(0);
            log.info("Last booking for item id #{} found", itemId);
            return new ItemBookingDto(booking.getId(), booking.getBooker().getId());
        }
    }

    public ItemBookingDto getNextBooking(Long itemId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findBookingByItem_IdAndStartIsAfterOrderByStart(itemId, currentTime);
        if (bookings.isEmpty()) {
            return null;
        } else {
            Booking booking = bookings.get(0);
            log.info("Next booking for item id #{} found", itemId);
            return new ItemBookingDto(booking.getId(), booking.getBooker().getId());
        }
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Long authorId = commentDto.getAuthorId();
        if (!bookingRepository.getBookedItemsIds(authorId, LocalDateTime.now()).contains(commentDto.getItem())) {
            throw new EntityNotFoundException("Item was not booked by author of comment");
        }
        Comment comment = CommentMapper.INSTANCE.toComment(commentDto);
        comment = commentRepository.save(comment);
        comment.getAuthor().setName(userService.getUserById(authorId).getName());
        log.info("Comment for Item id #{} created", comment.getItem());
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    public Collection<CommentDto> getCommentsByItemId(Long itemId) {
        return CommentMapper.INSTANCE.toCommentDtos(commentRepository.getCommentsByItemEquals(itemId));
    }

    public Item findById(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NoSuchElementException("Data not found!");
        }
        return itemOptional.get();
    }

}
