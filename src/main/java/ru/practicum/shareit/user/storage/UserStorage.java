package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long userId);

    Collection<User> getAllUsers();

    boolean isNotExistEmail(String email);

    boolean isExistUser(Long userId);
}
