package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemStorage.createItem(ItemMapper.INSTANCE.toItem(itemDto, userId));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("UItem with id #" + itemId + " didn't found!");
        }
        Item item = itemStorage.getItemById(itemId);
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.INSTANCE.toItem(itemDto, userId);
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

        return ItemMapper.INSTANCE.toItemDto(itemStorage.updateItem(item));
    }

    @Override
    public void deleteItem(long itemId) {
        itemStorage.deleteItem(itemId);
    }

    @Override
    public Collection<ItemDto> getItems(long userId) {
        return ItemMapper.INSTANCE.toItemDtos(itemStorage.getItems(userId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        text = text.toLowerCase();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return ItemMapper.INSTANCE.toItemDtos(itemStorage.searchItems(text));
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return itemStorage.isExistItem(itemId);
    }
}
