package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComments;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);

    ItemDtoWithComments toItemDtoWithComments(Item item);

    @Mapping(target = "owner", expression = "java(userId)")
    Item toItem(ItemDto itemDto, Long userId);

    Collection<ItemDto> toItemDtos(Collection<Item> items);
}
