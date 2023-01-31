package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
@ToString
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name="requestor_id")
    private Long requestor;
    private LocalDateTime created;
    @OneToMany(mappedBy = "requestId",cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();
}
