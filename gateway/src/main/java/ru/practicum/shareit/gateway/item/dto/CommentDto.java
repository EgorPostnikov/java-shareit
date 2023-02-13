package ru.practicum.shareit.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private String text;
    private Long item;
    private Long authorId;
    private String authorName;
}
