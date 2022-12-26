package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validation.ValidationException;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public UserDto getUserById(long userId) {
        if (!isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        return userMapper.toUserDto(userStorage.getUserById(userId));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User updatedUser = userStorage.getUserById(userId);
        user.setId(userId);
        if (user.getName() == null) {
            user.setName(updatedUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(updatedUser.getEmail());
        }
        if ((!user.getEmail().equals(updatedUser.getEmail())) && !isNotExistEmail(user.getEmail())) {
            throw new ValidationException("Email already in use.");
        }
        return userMapper.toUserDto(userStorage.updateUser(user));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userMapper.toUserDtos(userStorage.getAllUsers());
    }

    @Override
    public boolean isNotExistEmail(String email) {
        return userStorage.isNotExistEmail(email);
    }

    @Override
    public boolean isExistUser(Long userId) {
        return userStorage.isExistUser(userId);

    }
}
