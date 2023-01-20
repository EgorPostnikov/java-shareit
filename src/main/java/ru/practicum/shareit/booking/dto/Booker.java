package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@ToString

public class Booker {
    @Id
    private Long id;
}
