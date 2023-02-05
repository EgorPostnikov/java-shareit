package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
            "where i.available =true and (upper(i.description) like upper(concat('%', ?1, '%')) " +
            " or upper(i.name) like upper(concat('%', ?1, '%'))) ")
    Collection<Item> searchItems(String text);

    Collection<Item> getItemsByOwnerOrderById(long userId, PageRequest pageRequest);

}
