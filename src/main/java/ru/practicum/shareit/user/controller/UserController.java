package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Response;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.Create;
import ru.practicum.shareit.user.validation.Update;
import ru.practicum.shareit.user.validation.ValidationException;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
        private final UserService userService;

        @PostMapping
        @ResponseStatus(HttpStatus.OK)
        public UserDto createUser(@Validated(Create.class) @RequestBody UserDto user) {
            return userService.createUser(user);
        }

        @GetMapping("/{userId}")
        @ResponseStatus(HttpStatus.OK)
        public UserDto getUser(@PathVariable long userId) {
                return userService.getUser(userId);
        }

        @PatchMapping("/{userId}")
        @ResponseStatus(HttpStatus.OK)
        public UserDto updateUser(@PathVariable long userId,
                                  @RequestBody UserDto user) {
                return userService.updateUser(userId, user);
        }

        @DeleteMapping("/{userId}")
        @ResponseStatus(HttpStatus.OK)
        public void deleteUser(@PathVariable long userId) {
                userService.deleteUser(userId);
        }

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Collection<UserDto> getAllUsers() {
                return userService.getAllUsers();
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
