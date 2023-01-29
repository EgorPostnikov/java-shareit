package ru.practicum.shareit.request;

import org.apache.catalina.connector.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findItemRequestsByRequestorOrderByCreatedDesc(Long requestorId);
    Collection<ItemRequest> findItemRequestsByRequestorNotOrderByCreatedDesc(Long requestorId);
}

