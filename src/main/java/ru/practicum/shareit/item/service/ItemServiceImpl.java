package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserService userService;

    public ItemServiceImpl(ItemStorage itemInterface, ItemMapper itemMapper, UserService userService) {
        this.itemStorage = itemInterface;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemStorage.createItem(itemMapper.toItem(itemDto, userId));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("UItem with id #" + itemId + " didn't found!");
        }
        Item item = itemStorage.getItemById(itemId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto, userId);
        Item updatedItem = itemStorage.getItemById(itemDto.getId());
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

        return itemMapper.toItemDto(itemStorage.updateItem(item));
    }

    @Override
    public void deleteItem(long itemId) {
        itemStorage.deleteItem(itemId);
    }

    @Override
    public Collection<ItemDto> getItems(long userId) {
        return itemMapper.toItemDtos(itemStorage.getItems(userId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        text = text.toLowerCase();
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemMapper.toItemDtos(itemStorage.searchItems(text));
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return itemStorage.isExistItem(itemId);
    }
}
