package ru.practicum.shareit.unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Mock
    UserRepository mockUserRepository;

    @Test
    void testCreateItemWithMockOk() {

        User user = new User(1L, "Name", "email@mail.ru");
        UserDto userDto = new UserDto(1L, "Name", "email@mail.ru");

        UserServiceImpl userServiceImpl = new UserServiceImpl(null);
        userServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto GotUserDto = userServiceImpl.createUser(userDto);

        Assertions.assertEquals(1L, GotUserDto.getId());
    }
}