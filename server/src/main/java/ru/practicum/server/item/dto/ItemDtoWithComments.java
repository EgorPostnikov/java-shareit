package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
public class ItemDtoWithComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
    private Collection<CommentDto> comments;

}

