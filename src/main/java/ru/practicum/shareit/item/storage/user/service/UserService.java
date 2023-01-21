package ru.practicum.shareit.item.storage.user.service;

import ru.practicum.shareit.item.storage.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUserById(long userId);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);

    Collection<UserDto> getAllUsers();

    boolean isNotExistEmail(String email);

    boolean isExistUser(Long userId);
}
