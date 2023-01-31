package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findItemRequestsByRequestorOrderByCreatedDesc(Long requestorId);

    Collection<ItemRequest> findItemRequestsByRequestorNotOrderByCreatedDesc(Long requestorId, PageRequest sortingForRequest);
}

