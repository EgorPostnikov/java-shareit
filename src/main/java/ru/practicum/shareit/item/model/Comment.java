package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private Long id;
    private String text;
    private Long item;
    private Long author;
    private LocalDateTime created;
}
