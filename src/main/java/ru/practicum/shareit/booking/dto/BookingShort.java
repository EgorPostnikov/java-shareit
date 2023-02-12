package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.StartTimeBeforeEndTimeValidation;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingShort {
    private Long id;
    private Long itemId;
    @Future(message = "Time in the past.", groups = {Create.class})
    private LocalDateTime start;
    @Future(message = "Time in the past.", groups = {Create.class})
    private LocalDateTime end;

}
