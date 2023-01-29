package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class BookingShort {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}