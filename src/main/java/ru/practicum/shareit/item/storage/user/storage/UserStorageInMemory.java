package ru.practicum.shareit.item.storage.user.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.storage.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Repository
public class UserStorageInMemory implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserStorageInMemory.class);
    private final HashMap<Long, User> users = new HashMap<>();
    static Long id = 0L;

    public Long takeId() {
        return ++id;
    }

    @Override
    public User createUser(User user) {
        user.setId(takeId());
        users.put(user.getId(), user);
        log.info("User with id {} saved", user.getId());
        return users.get(user.getId());
    }

    @Override
    public User getUserById(Long userId) throws NoSuchElementException {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " didn't found!");
        }
        return users.get(userId);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("User with id " + user.getId() + " didn't found!");
        }
        users.put(user.getId(), user);
        log.info("User with id {} updated", user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("List of all users got, users qty - {}", users.size());
        return users.values();
    }

    @Override
    public boolean isNotExistEmail(String email) {
        return users.values().stream()
                .map(User::getEmail)
                .noneMatch(e -> e.equals(email));
    }

    @Override
    public boolean isExistUser(Long userId) {
        return users.containsKey(userId);
    }

}
