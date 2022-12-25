package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.validation.Create;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;

    ItemRequest request;

    public Item(Long id, String name, String description, Boolean available,Long owner ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        //this.request = request;
    }

}
