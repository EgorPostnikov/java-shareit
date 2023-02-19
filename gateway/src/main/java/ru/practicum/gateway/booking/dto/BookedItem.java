package ru.practicum.gateway.booking.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
@ToString
public class BookedItem {
    @Id
    private Long id;
    private String name;
    @Column(name = "owner_id")
    private Long owner;
}
