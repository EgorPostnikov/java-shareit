package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
    return null;}

    @Override
    public ItemRequestDto getItemRequestById(long requestId, Long userId) {
        return null;}

    @Override
    public Collection<ItemRequestDto> getItemRequestsOfUser(long userId) {
        return null;}

    @Override
    public Collection<ItemRequestDto> getAnotherItemRequests(long userId, int from, int size) {
        return null;}
}
