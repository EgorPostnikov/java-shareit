package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUser(long userId);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);

    Collection<UserDto> getAllUsers();

    boolean isNotExistEmail(String email);
}
