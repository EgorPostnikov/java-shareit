package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
@Mapper
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);
    ItemRequestWithResponseDto toItemRequestWithResponseDto(ItemRequest itemRequest);
    @Mapping(target = "description", source = "description")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> toItemRequestDtos(Collection<ItemRequest> itemRequests);
    Collection<ItemRequestWithResponseDto> toItemRequestWithResponseDtos(Collection<ItemRequest> itemRequests);
}
