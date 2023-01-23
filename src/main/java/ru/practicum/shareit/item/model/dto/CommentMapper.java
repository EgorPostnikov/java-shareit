package ru.practicum.shareit.item.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBooking;

import java.util.Collection;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping (target ="id", source = "id")
    CommentDto toCommentDto(Comment comment);


    Comment toComment(CommentDto commentDto);

    Collection<CommentDto> toCommentDtos(Collection<Comment> comments);
}
