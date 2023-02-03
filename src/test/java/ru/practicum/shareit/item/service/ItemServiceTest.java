package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.NoSuchElementException;

@SpringBootTest
public class ItemServiceTest {
    @Test
    void testCreateItemWithMockOk() {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId, "Item name", "Item description", true, ownerId, null);
        ItemDto itemDto = new ItemDto(itemId, "Item name", "Item description", true, null, null, null);

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null,null);

        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        itemServiceImpl.setItemRepository(mockItemRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(true);

        ItemDto GotItemDto = itemServiceImpl.createItem(3L, itemDto);

        Assertions.assertEquals(1L, GotItemDto.getId());
    }

    @Test
    void testCreateItemWithMockWrongUser() {
        Long itemId = 1L;
        Long ownerId = 2L;
        Item item = new Item(itemId, "Item name", "Item description", true, ownerId, null);
        ItemDto itemDto = new ItemDto(itemId, "Item name", "Item description", true, null, null, null);

        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(null, null, null,null);

        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        itemServiceImpl.setItemRepository(mockItemRepository);
        UserService mockUserService = Mockito.mock(UserService.class);
        itemServiceImpl.setUserService(mockUserService);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);
        Mockito
                .when(mockUserService.isExistUser(Mockito.anyLong()))
                .thenReturn(false);

        final NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> itemServiceImpl.createItem(3L, itemDto));

        Assertions.assertEquals("User id did not found!", exception.getMessage());
    }
}
