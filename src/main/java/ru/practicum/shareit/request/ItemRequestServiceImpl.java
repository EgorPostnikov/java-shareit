package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;


    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest request = ItemRequestMapper.INSTANCE.toItemRequest(itemRequestDto);
        if (!userService.isExistUser(userId)){
            throw new NoSuchElementException("User id did not found!");
        }
        request.setRequestor(userId);
        request=itemRequestRepository.save(request);
        log.info("ItemRequest with id #{} saved", request.getId());
        return  ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(request));
    }

    @Override
    public Collection<ItemRequestWithResponseDto> getItemRequests(long userId) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorOrderByCreatedDesc(userId);
        return ItemRequestMapper.INSTANCE.toItemRequestWithResponseDtos(requests);
    }

    @Override
    public Collection<ItemRequestDto> getAnotherItemRequests(long userId, PageRequest sortingForRequest) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorNotOrderByCreatedDesc(userId,sortingForRequest);
        return ItemRequestMapper.INSTANCE.toItemRequestDtos(requests);
    }
    @Override
    public ItemRequestWithResponseDto getItemRequestById(long itemRequestId, Long userId) {
        ItemRequest request = itemRequestRepository.findById(itemRequestId).get();
        return  ItemRequestMapper.INSTANCE.toItemRequestWithResponseDto(request);
    }
}
