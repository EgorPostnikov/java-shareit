package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item createItem(Item item);

    Item getItem(long itemId);

    Item updateItem(Item item);

    void deleteItem(long itemId);

    Collection<Item> getItems(long userId);

    Collection<Item> searchItems(String text);
}