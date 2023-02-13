package ru.practicum.shareit.gateway.request.dto;

import lombok.*;
import ru.practicum.shareit.gateway.item.dto.ItemForRequest;
import ru.practicum.shareit.gateway.validation.Create;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemRequestDto {
    private Long id;
    @NotNull(message = "Description is required.", groups = {Create.class})
    private String description;
    private Long requestor;
    private LocalDateTime created = LocalDateTime.now();
    private Set<ItemForRequest> items = new HashSet<>();
}
