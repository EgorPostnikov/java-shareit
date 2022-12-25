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
    UserStorage userStorage;
    UserMapper userMapper;

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
    public UserDto getUser(long userId) throws NoSuchElementException {
        return userMapper.toUserDto(userStorage.getUser(userId));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User updatedUser = userStorage.getUser(userId);
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
}
