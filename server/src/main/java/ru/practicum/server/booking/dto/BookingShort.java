package ru.practicum.server.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingShort {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}
