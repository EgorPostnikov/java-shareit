package ru.practicum.shareit.request.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.response.Response;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequestor(userId);
        return itemRequestService.createItemRequest(itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getAnotherItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "100") Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.unsorted());
        return itemRequestService.getAnotherItemRequests(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(requestId, userId);
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


}
