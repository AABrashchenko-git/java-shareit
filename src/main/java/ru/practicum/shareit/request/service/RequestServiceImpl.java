package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemRequestDto> getAllRequestsByOwner(Integer ownerId) {
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("user not found"));

        return requestRepository.findAllByRequestorId(ownerId).stream()
                .map(ItemRequestMapper::itemRequestToitemRequestDto)
                .peek(rDto -> {
                    List<ItemDto> itemsOfRequest = itemRepository.findAllByOwnerId(ownerId).stream()
                            .map(ItemMapper::itemToItemDto)
                            .collect(Collectors.toList());
                    rDto.setItems(itemsOfRequest);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequestsOfOtherUsers(Integer ownerId) {
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("user not found"));
        List<ItemRequest> requests = requestRepository.getAllRequestsOfOtherUsers(ownerId);
        return requests.stream()
                .map(ItemRequestMapper::itemRequestToitemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getItemRequest(Integer userId, Integer requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request is not found"));
        ItemRequestDto requestDto = ItemRequestMapper.itemRequestToitemRequestDto(request);
        requestDto.setRequestorId(request.getRequestor().getId());

        List<ItemDto> itemsOfRequest = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
        requestDto.setItems(itemsOfRequest);

        return requestDto;
    }

    @Transactional
    @Override
    public ItemRequestDto saveItemRequest(Integer requestorId, ItemRequestDto itemRequestDto) {
        User requestor = userRepository.findById(requestorId).orElseThrow(() -> new NotFoundException("Requestor not found"));
        itemRequestDto.setRequestorId(requestorId);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest request = ItemRequestMapper.itemRequestDtoToitemRequest(itemRequestDto);
        request.setRequestor(requestor);
        return ItemRequestMapper.itemRequestToitemRequestDto(requestRepository.save(request));
    }
}
