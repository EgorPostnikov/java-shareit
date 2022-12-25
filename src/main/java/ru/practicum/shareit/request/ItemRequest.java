package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ItemRequest {
    Long id;
    String description;
    Long requestor;
    LocalDateTime created;
}
