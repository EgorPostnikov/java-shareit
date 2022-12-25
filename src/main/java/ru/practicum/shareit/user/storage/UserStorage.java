package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.NoSuchElementException;

public interface UserStorage {

    User createUser(User user);

    User getUser(Long id);

    User updateUser(User user);

    void deleteUser(Long userId);

    Collection<User> getAllUsers();

    boolean isNotExistEmail(String email);
}
