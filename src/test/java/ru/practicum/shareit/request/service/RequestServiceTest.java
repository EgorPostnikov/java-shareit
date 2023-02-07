package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.ItemForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;


@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    UserService mockUserService;
    @Mock
    ItemRequestRepository mockItemRequestRepository;
    ItemForRequest itemForRequest = new ItemForRequest(
            1L,
            "Item",
            "Description",
            true,
            1L,
            1L);
    ItemForRequest itemForRequest2 = new ItemForRequest(
            1L,
            "Item",
            "Description",
            true,
            1L,
            1L);
    ItemRequest itemRequest = new ItemRequest(
            1L,
            "Описание тест",
            1L,
            LocalDateTime.now(),
            new HashSet<>());

    @Test
    void tesGetItemRequestByIdWithMockOk() {
        ItemRequest itemRequest2 = itemRequest;
        itemRequest2.getItems().add(itemForRequest);
        itemRequest2.getItems().add(itemForRequest2);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest2));
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        ItemRequestDto itemRequestDto = itemRequestServiceImpl.getItemRequestById(1L, 1L);
        ItemForRequest item = itemRequest2.getItems().stream().findFirst().get();
        Assertions.assertEquals("Описание тест", itemRequestDto.getDescription());
        Assertions.assertEquals(itemRequest.getId(), itemRequestDto.getId());
        Assertions.assertEquals(itemRequest.getRequestor(), itemRequestDto.getRequestor());
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(item.getId(), itemForRequest2.getId());
        Assertions.assertEquals(item.getName(), itemForRequest2.getName());
        Assertions.assertEquals(item.getDescription(), itemForRequest2.getDescription());
        Assertions.assertEquals(item.getRequestId(), itemForRequest2.getRequestId());
        Assertions.assertEquals(item.getOwner(), itemForRequest2.getOwner());
        Assertions.assertEquals(item.getAvailable(), itemForRequest2.getAvailable());
    }

    @Test
    void tesGetItemRequestByIdWithMockWrongUser() {
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemRequestServiceImpl.getItemRequestById(1L, 1L));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }

    @Test
    void getItemRequests() {
        Collection<ItemRequest> requests = new ArrayList<>();
        requests.add(itemRequest);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockUserService.getUserById(Mockito.eq(1L)))
                .thenReturn(new UserDto(1L, "name", "mail@mail.ru"));

        Mockito
                .when(mockItemRequestRepository.findItemRequestsByRequestorOrderByCreatedDesc(Mockito.eq(1L)))
                .thenReturn(requests);

        Collection<ItemRequestDto> gotRequests = itemRequestServiceImpl.getItemRequests(1L);
        ItemRequestDto gotRequest = gotRequests.stream().findFirst().get();
        Assertions.assertEquals(1, gotRequests.size());
        Assertions.assertEquals(itemRequest.getId(), gotRequest.getId());
        Assertions.assertEquals(itemRequest.getRequestor(), gotRequest.getRequestor());
        Assertions.assertEquals(itemRequest.getDescription(), gotRequest.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), gotRequest.getCreated());
        Assertions.assertEquals(itemRequest.getItems(), gotRequest.getItems());
    }

    @Test
    void getAnotherItemRequests() {
        Collection<ItemRequest> requests = new ArrayList<>();
        requests.add(itemRequest);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockUserService.getUserById(Mockito.eq(1L)))
                .thenReturn(new UserDto(1L, "name", "mail@mail.ru"));

        Mockito
                .when(mockItemRequestRepository.findItemRequestsByRequestorNotOrderByCreatedDesc(Mockito.eq(1L), Mockito.any()))
                .thenReturn(requests);
        PageRequest pageRequest = PageRequest.of(0, 100, Sort.unsorted());
        Collection<ItemRequestDto> gotRequests = itemRequestServiceImpl.getAnotherItemRequests(1L, pageRequest);
        ItemRequestDto gotRequest = gotRequests.stream().findFirst().get();
        Assertions.assertEquals(1, gotRequests.size());
        Assertions.assertEquals(itemRequest.getId(), gotRequest.getId());
        Assertions.assertEquals(itemRequest.getRequestor(), gotRequest.getRequestor());
        Assertions.assertEquals(itemRequest.getDescription(), gotRequest.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), gotRequest.getCreated());
        Assertions.assertEquals(itemRequest.getItems(), gotRequest.getItems());

    }

    @Test
    void getItemRequestById() {
        Collection<ItemRequest> requests = new ArrayList<>();
        requests.add(itemRequest);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestDto gotItemRequest = itemRequestServiceImpl.getItemRequestById(1L, 1L);

        Assertions.assertEquals(itemRequest.getId(), gotItemRequest.getId());
        Assertions.assertEquals(itemRequest.getRequestor(), gotItemRequest.getRequestor());
        Assertions.assertEquals(itemRequest.getDescription(), gotItemRequest.getDescription());
        Assertions.assertEquals(itemRequest.getCreated(), gotItemRequest.getCreated());
        Assertions.assertEquals(itemRequest.getItems(), gotItemRequest.getItems());
    }

    @Test
    void getItemRequestById2() {
        Collection<ItemRequest> requests = new ArrayList<>();
        requests.add(itemRequest);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemRequestServiceImpl.getItemRequestById(1L, 1L));

        Assertions.assertEquals("Request id did not found!", exception.getMessage());
    }

    @Test
    void getItemRequestById3() {
        Collection<ItemRequest> requests = new ArrayList<>();
        requests.add(itemRequest);
        ItemRequestServiceImpl itemRequestServiceImpl = new ItemRequestServiceImpl(null, null);
        itemRequestServiceImpl.setItemRequestRepository(mockItemRequestRepository);
        itemRequestServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRequestRepository.existsById(Mockito.eq(1L)))
                .thenReturn(true);
        Mockito
                .when(mockUserService.isExistUser(Mockito.eq(1L)))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemRequestServiceImpl.getItemRequestById(1L, 1L));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }
}


