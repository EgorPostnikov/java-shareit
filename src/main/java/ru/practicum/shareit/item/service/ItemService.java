package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.InvalidAccessException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto item);

    ItemDtoWithComments getItemById(long itemId, Long serId);

    Item getItem(long itemId);

    Long getOwnerOfItem(long itemId);

    ItemDto updateItem(long userId, ItemDto item) throws InvalidAccessException;

    void deleteItem(long itemId);

    Collection<ItemDtoWithComments> getItems(long userId, PageRequest pageRequest);

    Collection<ItemDto> searchItems(String text);

    boolean isExistItem(Long itemId);

    CommentDto createComment(CommentDto commentDto);
}
