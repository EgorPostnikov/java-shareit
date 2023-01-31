package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
@ToString
public class ItemForRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @Column(name = "owner_id")
    private Long owner;
    @Column(name = "request_id")
    private Long requestId;
}
