package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestWithResponseDto {
        private Long id;
        private String description;
        private Long requestor;
        private LocalDateTime created;
        private Set<Item> items;
}
