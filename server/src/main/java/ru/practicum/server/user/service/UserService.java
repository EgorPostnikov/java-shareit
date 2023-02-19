package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto getUserById(long userId);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);

    Collection<UserDto> getAllUsers();

    boolean isExistEmail(String email);

    boolean isExistUser(Long userId);
}
