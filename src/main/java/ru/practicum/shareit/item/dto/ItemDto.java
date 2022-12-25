package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class ItemDto {
    Long id;
    @NotNull(message = "Name is required.", groups = {Create.class})
    @NotBlank (message = "Name is required.", groups = {Create.class})
    String name;
    @NotNull(message = "Description is required.", groups = {Create.class})
    @NotBlank (message = "Description is required.", groups = {Create.class})
    String description;
    @NotNull(message = "Availability is required.", groups = {Create.class})
    Boolean available;
    Long requestId;

    public ItemDto( Long id, String name, String description, Boolean available, Long requestId) {
        this.id =id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
