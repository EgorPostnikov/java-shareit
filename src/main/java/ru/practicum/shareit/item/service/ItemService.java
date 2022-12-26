package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto item);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long userId, ItemDto item);

    void deleteItem(long itemId);

    Collection<ItemDto> getItems(long userId);

    Collection<ItemDto> searchItems(String text);

    boolean isExistItem(Long itemId);
}
