package ru.practicum.shareit.item.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.validation.Create;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto item) {
        item.setId(itemId);
        return itemService.updateItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        return new Response(exception.getMessage());
    }

}
