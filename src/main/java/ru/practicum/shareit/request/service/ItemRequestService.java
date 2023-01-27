package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto getItemRequestById(long requestId, Long userId);

    Collection<ItemRequestDto> getItemRequestsOfUser(long userId);

    Collection<ItemRequestDto> getAnotherItemRequests(long userId, int from, int size);
}
