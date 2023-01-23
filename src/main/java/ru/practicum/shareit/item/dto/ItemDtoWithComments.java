package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
@Setter
@Getter
@AllArgsConstructor
public class ItemDtoWithComments {
        private Long id;
        @NotNull(message = "Name is required.", groups = {Create.class})
        @NotBlank(message = "Name is required.", groups = {Create.class})
        private String name;
        @NotNull(message = "Description is required.", groups = {Create.class})
        @NotBlank(message = "Description is required.", groups = {Create.class})
        private String description;
        @NotNull(message = "Availability is required.", groups = {Create.class})
        private Boolean available;
        private Long requestId;
        private ItemBookingDto lastBooking;
        private ItemBookingDto nextBooking;
        private Collection<CommentDto> comments;

    }

