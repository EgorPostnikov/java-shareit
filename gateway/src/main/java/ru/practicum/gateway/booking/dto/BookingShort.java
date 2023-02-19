package ru.practicum.gateway.booking.dto;

import lombok.*;
import ru.practicum.gateway.validation.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingShort {
    private Long id;
    private Long itemId;
    @FutureOrPresent(message = "Time in the past.", groups = {Create.class})
    private LocalDateTime start;
    @Future(message = "Time in the past.", groups = {Create.class})
    private LocalDateTime end;

}
