package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotNull(message = "Description is required.", groups = {Create.class})
    private String description;
    private Long requestor;
    private LocalDateTime created;
    private Collection<Item> items;
}