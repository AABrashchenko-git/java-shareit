package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.util.List;

public interface RequestService {
    List<ItemRequestDto> getAllRequestsByOwner(Integer ownerId);

    List<ItemRequestDto> getAllRequestsOfOtherUsers(Integer ownerId);

    ItemRequestDto getItemRequest(Integer ownerId, Integer requestId);

    ItemRequestDto saveItemRequest(Integer ownerId, ItemRequestDto itemRequestDto);
}
