package ru.practicum.shareit.request.mapper;

import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    //@Mapping(target = "requestId", source = "item.request.id")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    //@Mapping(target = "owner", source = "userId")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Long userId);

    Collection<ItemRequestDto> toItemRequestDtos(Collection<ItemRequest> itemRequests);
}
