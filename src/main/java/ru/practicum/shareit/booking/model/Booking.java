package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private BookedItem item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    private Booker booker;
    @Enumerated(EnumType.STRING)
    private Status status;
}
