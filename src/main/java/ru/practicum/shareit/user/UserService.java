package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getItems(long userId);

    User addNewItem(Long userId, User item);

    void deleteItem(long userId, long itemId);
}
