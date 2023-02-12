package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> getItemRequests(long userId);

    ItemRequestDto getItemRequestById(long itemRequestId, Long userId);

    Collection<ItemRequestDto> getAnotherItemRequests(long userId, PageRequest sortingForRequest);
}
