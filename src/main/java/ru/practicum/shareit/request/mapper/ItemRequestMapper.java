package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest itemRequestDtoToitemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder().id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestorId(itemRequestDto.getRequestorId())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public ItemRequestDto itemRequestToitemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder().id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestorId())
                .created(itemRequest.getCreated())
                .build();
    }
}
