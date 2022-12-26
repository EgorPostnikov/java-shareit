package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public Item toItem(ItemDto itemDto, Long userId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                new ItemRequest()
        );
    }

    public Collection<ItemDto> toItemDtos(Collection<Item> items) {
        Collection<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }
}
