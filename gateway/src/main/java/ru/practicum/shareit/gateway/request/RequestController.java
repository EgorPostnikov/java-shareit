package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import ru.practicum.shareit.gateway.response.Response;
import ru.practicum.shareit.gateway.validation.Create;
import ru.practicum.shareit.gateway.validation.ValidationException;


import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
//@Validated
public class RequestController {
    RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequestor(userId);
        log.info("Creating request {}, userId={}", itemRequestDto, userId);
        return requestClient.createItemRequest(itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Getting requests, for userId={}", userId);
        return requestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAnotherItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "100") Integer size) {
        log.info("Getting requests,not for userId={}", userId);
        return requestClient.getAnotherItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable long requestId) {
        log.info("Getting requests requestId={}, userId={}", requestId, userId);
        return requestClient.getItemRequestById(requestId, userId);
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
