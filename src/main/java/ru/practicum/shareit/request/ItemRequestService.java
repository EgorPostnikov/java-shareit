package ru.practicum.shareit.request;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> getItemRequests(long userId);

    ItemRequestDto getItemRequestById(long itemRequestId, Long userId);

    Collection<ItemRequestDto> getAnotherItemRequests(long userId, Integer from, Integer size);
}
