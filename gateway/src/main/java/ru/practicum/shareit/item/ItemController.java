package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidAccessException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.validation.Create;


import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Create item {}, userId={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable long itemId) {
        log.info("Get item by id {}, userId={}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto item) throws InvalidAccessException {
        item.setId(itemId);
        log.info("Update item by id {}, userId={}", itemId, userId);
        return itemClient.updateItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteItem(@PathVariable long itemId) {
        log.info("Delete item by id {}", itemId);
        return itemClient.deleteItem(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "100") Integer size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Search items by text = {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(@PathVariable long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated(Create.class) @RequestBody CommentDto commentDto) {
        commentDto.setItem(itemId);
        commentDto.setAuthorId(userId);
        log.info("Create comment {}, from itemId={}, userId={}", commentDto, itemId, userId);
        return itemClient.createComment(commentDto);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Response handleException(EntityNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidAccessException.class)
    public Response handleException(InvalidAccessException exception) {
        return new Response(exception.getMessage());
    }


}
