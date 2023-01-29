package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;


    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest request = ItemRequestMapper.INSTANCE.toItemRequest(itemRequestDto);
        request.setRequestor(userId);
        request=itemRequestRepository.save(request);
        log.info("ItemRequest with id #{} saved", request.getId());
        return  ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(request));
    }

    @Override
    public Collection<ItemRequestDto> getItemRequests(long userId) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorOrderByCreatedDesc(userId);
        return ItemRequestMapper.INSTANCE.toItemRequestDtos(requests);
    }

    @Override
    public Collection<ItemRequestDto> getAnotherItemRequests(long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorNotOrderByCreatedDesc(userId);
        return ItemRequestMapper.INSTANCE.toItemRequestDtos(requests);
    }
    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId, Long userId) {
        ItemRequest request = itemRequestRepository.findById(itemRequestId).get();
        return  ItemRequestMapper.INSTANCE.toItemRequestDto(request);
    }
}
