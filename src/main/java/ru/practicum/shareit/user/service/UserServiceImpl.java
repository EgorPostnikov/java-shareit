package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validation.ValidationException;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.toUser(userDto);
        User createdUser = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(createdUser);
    }

    @Override
    public UserDto getUserById(long userId) {
        if (!isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        return UserMapper.INSTANCE.toUserDto(userRepository.getById(userId));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = UserMapper.INSTANCE.toUser(userDto);
        User updatedUser = userRepository.getById(userId);
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
        return UserMapper.INSTANCE.toUserDto(userRepository.saveAndFlush(updatedUser));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return UserMapper.INSTANCE.toUserDtos(userRepository.findAll());
    }

    @Override
    public boolean isNotExistEmail(String email) {
        return userStorage.isNotExistEmail(email);
    }

    @Override
    public boolean isExistUser(Long userId) {
        return userRepository.existsById(userId);

    }
}
