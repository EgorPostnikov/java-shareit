package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(" select i from Item i " +
            "where i.available =true and (upper(i.description) like upper(concat('%', ?1, '%')) " +
            " or upper(i.name) like upper(concat('%', ?1, '%'))) ")
    Collection<Long> searchItems (Long userId);
}
