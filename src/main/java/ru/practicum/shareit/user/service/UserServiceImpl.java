package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Setter
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private  UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.toUser(userDto);
        User createdUser;
        createdUser = userRepository.save(user);
        log.info("User with id #{} saved", createdUser.getId());
        return UserMapper.INSTANCE.toUserDto(createdUser);
    }

    @Override
    public UserDto getUserById(long userId) {
        if (!isExistUser(userId)) {
            throw new NoSuchElementException("User with id #" + userId + " didn't found!");
        }
        log.info("User with id #{} found", userId);
        return UserMapper.INSTANCE.toUserDto(findById(userId));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = UserMapper.INSTANCE.toUser(userDto);
        User updatedUser = findById(userId);
        user.setId(userId);
        if (user.getName() == null) {
            user.setName(updatedUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(updatedUser.getEmail());
        }
        userRepository.save(user);
        log.info("User with id #{} updated", userId);
        return UserMapper.INSTANCE.toUserDto(findById(userId));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User with id #{} deleted", userId);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        log.info("Users list found, users quantity is #{}", users.size());
        return UserMapper.INSTANCE.toUserDtos(users);
    }

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean isExistUser(Long userId) {
        return userRepository.existsById(userId);

    }

    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("Data not found!");
        }
        return userOptional.get();
    }
}
