package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Entity
@ToString
@Table(name= "bookings",schema = "public" )
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "item_id")
    private Long item;
    @Column (name = "start_date")
    private LocalDateTime start;
    @Column (name = "end_date")
    private LocalDateTime end;
    @Column (name = "booker_id")
    private Long booker;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking() {

    }
}
