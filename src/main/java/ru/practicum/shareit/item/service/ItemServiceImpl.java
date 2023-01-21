package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.ItemBooking;
import ru.practicum.shareit.item.model.dto.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.dto.ItemDto;
import ru.practicum.shareit.item.model.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;


    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemRepository.save(ItemMapper.INSTANCE.toItem(itemDto, userId));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        Item item = itemRepository.getById(itemId);
        ItemDto itemDto = ItemMapper.INSTANCE.toItemDto(item);
        itemDto.setNextBooking(getNextBooking(item.getId()));
        itemDto.setLastBooking(getLastBooking(item.getId()));
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
    public Collection<ItemDto> getItems(long userId) {
        Collection<Item> items = itemRepository.getItems(userId);
        if (items == null) {
            return null;
        } else {
            Collection<ItemDto> collection = new ArrayList(items.size());
            Iterator var3 = items.iterator();
            while (var3.hasNext()) {
                Item item = (Item) var3.next();
                ItemDto itemDto = ItemMapper.INSTANCE.toItemDto(item);
                itemDto.setNextBooking(getNextBooking(item.getId()));
                itemDto.setLastBooking(getLastBooking(item.getId()));
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

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        return null;
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
}
