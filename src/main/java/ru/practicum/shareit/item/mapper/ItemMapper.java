package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;

@UtilityClass
public class ItemMapper {
    public Item itemDtoToItem(ItemDto itemDto) {
        return Item.builder().id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(itemDto.getOwnerId())
                .build();
    }

    public ItemDto itemToItemDto(Item item) {
        return ItemDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .build();
    }

    public ItemResponseOnlyDto itemToItemResponseDto(Item item) {
        return ItemResponseOnlyDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
               // .ownerId(item.getOwnerId())
                .build();
    }
}
