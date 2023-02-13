package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Long item;
    private Long authorId;
    private String authorName;
}
