package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUser(long userId);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);

    Collection<UserDto> getAllUsers();

    boolean isNotExistEmail(String email);
}
