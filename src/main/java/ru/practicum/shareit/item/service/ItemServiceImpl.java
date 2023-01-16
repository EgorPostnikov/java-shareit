package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
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
    private final ItemRepository itemRepository;


    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = itemRepository.save(ItemMapper.INSTANCE.toItem(itemDto, userId));
        return ItemMapper.INSTANCE.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        Item item = itemRepository.getById(itemId);
        return ItemMapper.INSTANCE.toItemDto(item);
    }
    @Override
    public Item getItem(long itemId) {
        if (!isExistItem(itemId)) {
            throw new NoSuchElementException("Item with id #" + itemId + " didn't found!");
        }
        return itemRepository.getById(itemId);
    }
    @Override
    public Long getOwnerOfItem(long itemId) {
        return itemRepository.getById(itemId).getOwner();
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.INSTANCE.toItem(itemDto, userId);
        Item updatedItem = itemRepository.getById(itemDto.getId());
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

        return ItemMapper.INSTANCE.toItemDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemDto> getItems(long userId) {
        return ItemMapper.INSTANCE.toItemDtos(itemRepository.getItems(userId));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        text = text.toLowerCase();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return ItemMapper.INSTANCE.toItemDtos(itemRepository.searchItems(text));
    }

    @Override
    public boolean isExistItem(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        return null;
    }
}
