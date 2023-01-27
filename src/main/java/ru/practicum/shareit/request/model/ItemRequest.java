package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class ItemRequest {
    @Id
    private Long id;
    private String description;
    @Column(name="requestor_id")
    private Long requestor;
    private LocalDateTime created =LocalDateTime.now();
}
