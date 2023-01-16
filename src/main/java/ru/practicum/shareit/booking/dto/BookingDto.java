package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class BookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}

