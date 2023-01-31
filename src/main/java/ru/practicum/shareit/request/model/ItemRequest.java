package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.ItemForRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "requestor_id")
    private Long requestor;
    private LocalDateTime created;
    @OneToMany(mappedBy = "requestId", cascade = CascadeType.ALL)
    private Set<ItemForRequest> items = new HashSet<>();
}
