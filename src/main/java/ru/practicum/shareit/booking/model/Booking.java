package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookedItem;
import ru.practicum.shareit.booking.dto.Booker;
import ru.practicum.shareit.user.model.User;

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
    @Column (name = "start_date")
    private LocalDateTime start;
    @Column (name = "end_date")
    private LocalDateTime end;
    //@Transient
    @ManyToOne (fetch=FetchType.EAGER)
    @JoinColumn(name="item_id")
    private BookedItem item;
    @ManyToOne (fetch=FetchType.EAGER)
    @JoinColumn (name="booker_id")
    private Booker booker;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking() {
    }
}
