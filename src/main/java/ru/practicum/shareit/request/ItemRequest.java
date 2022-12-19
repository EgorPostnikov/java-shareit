package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    Long id;
    String description;
    Long requestor;
    LocalDateTime created;
}
