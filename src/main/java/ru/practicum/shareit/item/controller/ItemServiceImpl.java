package ru.practicum.shareit.item.controller;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.controller.UserService;
import ru.practicum.shareit.user.validation.ValidationException;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;


@Service
public class ItemServiceImpl implements ItemService {

    ItemStorage itemStorage;
    ItemMapper itemMapper;
    UserService userService;

    public ItemServiceImpl(ItemStorage itemInterface, ItemMapper itemMapper, UserService userService) {
        this.itemStorage = itemInterface;
        this.itemMapper = itemMapper;
        this.userService = userService;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUser(userId);
        Item item = itemStorage.createItem(itemMapper.toItem(itemDto,userId));
        return itemMapper.toItemDto(item) ;
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = itemStorage.getItem(itemId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto, userId);
        Item updatedItem = itemStorage.getItem(itemDto.getId());
        if (!(updatedItem.getOwner()==userId)) {
            throw new NoSuchElementException("Access rights are not defined");
        }
            userService.getUser(userId);
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
        if (text.isBlank()){
            return Collections.emptyList();
        }
        return itemMapper.toItemDtos(itemStorage.searchItems(text));
    }
}
