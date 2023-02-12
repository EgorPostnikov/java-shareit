package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository mockUserRepository;
    User user = new User(
            1L,
            "Name",
            "email@mail.ru");
    User user2 = new User(
            2L,
            "Name2",
            "email2@mail.ru");
    UserDto userDto = new UserDto(
            1L,
            "Name",
            "email@mail.ru");

    @Test
    void testCreateItemWithMockOk() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        UserDto gotUserDto = userServiceImpl.createUser(userDto);
        Assertions.assertEquals(userDto.getId(), gotUserDto.getId());
        Assertions.assertEquals(userDto.getName(), gotUserDto.getName());
        Assertions.assertEquals(userDto.getEmail(), gotUserDto.getEmail());
    }

    @Test
    void isExistEmail() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.existsUserByEmail(Mockito.eq("mail")))
                .thenReturn(true);

        Boolean isExistEmail = userServiceImpl.isExistEmail("mail");
        Assertions.assertEquals(true, isExistEmail);

    }

    @Test
    void isExistUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);

        Boolean isExistUser = userServiceImpl.isExistUser(1L);
        Assertions.assertEquals(true, isExistUser);

    }

    @Test
    void findById() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));

        User gotUser = userServiceImpl.findById(1L);
        Assertions.assertEquals(user.getId(), gotUser.getId());
        Assertions.assertEquals(user.getName(), gotUser.getName());
        Assertions.assertEquals(user.getEmail(), gotUser.getEmail());

    }

    @Test
    void findById2() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(null));

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userServiceImpl.findById(1L));

        Assertions.assertEquals("Data not found!", exception.getMessage());

    }

    @Test
    void getUserById() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockUserRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));

        UserDto gotUserDto = userServiceImpl.getUserById(1L);
        Assertions.assertEquals(user.getId(), gotUserDto.getId());
        Assertions.assertEquals(user.getName(), gotUserDto.getName());
        Assertions.assertEquals(user.getEmail(), gotUserDto.getEmail());
    }

    @Test
    void getUserById2() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.existsById(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> userServiceImpl.getUserById(1L));

        Assertions.assertEquals("User with id #" + 1L + " didn't found!", exception.getMessage());
    }

    @Test
    void updateUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        UserDto gotUserDto = userServiceImpl.updateUser(userDto);
        Assertions.assertEquals(user.getId(), gotUserDto.getId());
        Assertions.assertEquals(user.getName(), gotUserDto.getName());
        Assertions.assertEquals(user.getEmail(), gotUserDto.getEmail());
    }

    @Test
    void getAllUsers() {
        Collection<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn((List<User>) users);

        Collection<UserDto> gotUsers = userServiceImpl.getAllUsers();
        Assertions.assertEquals(2, gotUsers.size());

    }

}