package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestWithResponseDto> getItemRequests(long userId);

    ItemRequestWithResponseDto getItemRequestById(long itemRequestId, Long userId);

    Collection<ItemRequestDto> getAnotherItemRequests(long userId, PageRequest sortingForRequest);
}
