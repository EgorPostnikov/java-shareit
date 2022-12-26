package ru.practicum.shareit.item.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Repository
public class ItemStorageInMemory implements ItemStorage {

    private static final Logger log = LoggerFactory.getLogger(ItemStorageInMemory.class);
    private final HashMap<Long, Item> items = new HashMap<>();
    static Long id = 0L;

    public Long takeId() {
        return ++id;
    }

    @Override
    public Item createItem(Item item) {
        Long itemId = takeId();
        item.setId(itemId);
        items.put(itemId, item);
        log.info("Item with id #{} saved", itemId);
        return items.get(itemId);
    }

    @Override
    public Item getItemById(long itemId) {
        log.info("Item with id #{} got", itemId);
        return items.get(itemId);
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Item with id #{} updated", item.getId());
        return items.get(item.getId());
    }

    @Override
    public void deleteItem(long itemId) {
        log.info("Item with id #{} deleted", itemId);
        items.remove(itemId);
    }

    @Override
    public Collection<Item> getItems(long userId) {
        log.info("List of items of user #{}   get", userId);
        return items.values().stream()
                .filter(t -> t.getOwner() == userId)
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Item> searchItems(String text) {
        log.info("List of items, containing text -{}- got", text);
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(t -> (t.getName().toLowerCase().contains(text) || t.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return items.containsKey(itemId);
    }
}
