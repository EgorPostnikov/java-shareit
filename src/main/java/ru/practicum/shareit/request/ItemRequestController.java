package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * TODO Sprint add-item-requests.
 */
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
                              @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestWithResponseDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequests(userId);
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> getAnotherItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam (defaultValue = "0") Integer from,
                                                             @RequestParam (defaultValue = "100") Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.unsorted());
        return itemRequestService.getAnotherItemRequests(userId, pageRequest);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithResponseDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable long itemRequestId) {
        return itemRequestService.getItemRequestById(itemRequestId, userId);
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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidationException.class)
    public Response handleException(ValidationException exception) {
        return new Response(exception.getMessage());
    }


}
