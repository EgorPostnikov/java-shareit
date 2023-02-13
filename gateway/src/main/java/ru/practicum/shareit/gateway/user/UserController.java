package ru.practicum.shareit.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.response.Response;
import ru.practicum.shareit.gateway.validation.Create;
import ru.practicum.shareit.gateway.validation.ValidationException;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
//@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createUser(@Validated(Create.class) @RequestBody UserDto requestDto) {
        log.info("Creating user {}", requestDto);
        return userClient.createUser(requestDto);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Get user, userId={}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @RequestBody UserDto requestDto) {
        requestDto.setId(userId);
        log.info("Update User {}, userId={}", requestDto, userId);
        return userClient.updateUser(requestDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("Delete User, userId={}", userId);
        return userClient.deleteUser(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidationException.class)
    public Response handleException(ValidationException exception) {
        return new Response(exception.getMessage());
    }
}
