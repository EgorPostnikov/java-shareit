package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Setter

public class ItemRequestServiceImpl implements ItemRequestService {
    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;


    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest request = ItemRequestMapper.INSTANCE.toItemRequest(itemRequestDto);
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User id did not found!");
        }
        request.setRequestor(userId);
        request = itemRequestRepository.save(request);
        log.info("ItemRequest with id #{} saved", request.getId());
        return ItemRequestMapper.INSTANCE.toItemRequestDto(itemRequestRepository.save(request));
    }

    @Override
    public Collection<ItemRequestDto> getItemRequests(long userId) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorOrderByCreatedDesc(userId);
        log.info("List of ItemRequests found, requests qty is #{} saved", requests.size());
        return ItemRequestMapper.INSTANCE.toItemRequestDtos(requests);
    }

    @Override
    public Collection<ItemRequestDto> getAnotherItemRequests(long userId, PageRequest sortingForRequest) {
        userService.getUserById(userId);
        Collection<ItemRequest> requests = itemRequestRepository.
                findItemRequestsByRequestorNotOrderByCreatedDesc(userId, sortingForRequest);
        log.info("List of ItemRequests found, requests qty is #{} saved", requests.size());
        return ItemRequestMapper.INSTANCE.toItemRequestDtos(requests);
    }

    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId, Long userId) {
        if (!itemRequestRepository.existsById(itemRequestId)) {
            throw new NoSuchElementException("Request id did not found!");
        }
        if (!userService.isExistUser(userId)) {
            throw new NoSuchElementException("User id did not found!");
        }
        ItemRequest request = itemRequestRepository.findById(itemRequestId).get();
        log.info("ItemRequest with id #{} found", itemRequestId);
        return ItemRequestMapper.INSTANCE.toItemRequestDto(request);
    }
}
