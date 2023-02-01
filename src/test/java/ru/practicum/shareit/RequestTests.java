package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class RequestTests {
    @Test
    void tesGetItemRequestByIdWithMockOk() {
        ItemRequest itemRequest = new ItemRequest(1L,"Описание тест",1L, LocalDateTime.now(),null);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl();
        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        ItemRequestDto itemRequestDto = itemRequestServiceImpl.getItemRequestById(1L, 1L);

        Assertions.assertEquals("Описание тест", itemRequestDto.getDescription());

    }

    @Test
    void tesGetItemRequestByIdWithMockWrongUser() {
        ItemRequest itemRequest = new ItemRequest(1L,"Описание тест",1L, LocalDateTime.now(),null);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl();
        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemRequestServiceImpl.getItemRequestById(1L, 1L));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }

    @Test
    void tesGetItemRequestByIdWithMockWrongId() {
        ItemRequest itemRequest = new ItemRequest(1L,"Описание тест",1L, LocalDateTime.now(),null);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl();
        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.anyLong()))
                .thenReturn(false);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemRequestServiceImpl.getItemRequestById(100L, 1L));

        Assertions.assertEquals("Request id did not found!", exception.getMessage());
    }
}
