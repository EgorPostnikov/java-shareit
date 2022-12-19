package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getItems(long userId) {
        return null;
    }
    @Override
    public User addNewItem(Long userId, User item) {
        return null;
    }
    @Override
    public void deleteItem(long userId, long itemId) {
    }
}
