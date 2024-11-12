package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequest itemRequestDtoToitemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder().id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestorId(itemRequestDto.getRequestorId())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto itemRequestToitemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder().id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestorId())
                .created(itemRequest.getCreated())
                .build();
    }
}
