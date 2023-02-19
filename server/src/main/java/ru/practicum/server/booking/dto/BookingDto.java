package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.server.booking.model.BookedItem;
import ru.practicum.server.booking.model.Booker;
import ru.practicum.server.booking.model.Status;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookedItem item;
    private Booker booker;
    private Status status;
}

