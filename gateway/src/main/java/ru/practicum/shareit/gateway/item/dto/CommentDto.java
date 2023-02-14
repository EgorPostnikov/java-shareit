package ru.practicum.shareit.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.gateway.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    @NotNull(message = "Commentaries is required.", groups = {Create.class})
    @NotBlank(message = "Commentaries is required.", groups = {Create.class})
    private String text;
    private Long item;
    private Long authorId;
    private String authorName;
}
