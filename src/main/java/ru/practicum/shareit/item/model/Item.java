package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    ItemRequest request;

    public Boolean isAvailable() {
        return getAvailable();
    }
}
