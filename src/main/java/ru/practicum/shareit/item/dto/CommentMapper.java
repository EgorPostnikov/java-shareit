package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping (target ="id", source = "id")
    CommentDto toCommentDto(Comment comment);

    @Mapping (target ="authorId", source = "authorId")
    Comment toComment(CommentDto commentDto);

    Collection<CommentDto> toCommentDtos(Collection<Comment> comments);
}
