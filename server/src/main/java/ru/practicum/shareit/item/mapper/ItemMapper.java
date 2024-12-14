package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;

@UtilityClass
// убирал @UtilityClass, потому что проект ни в какую не собирался. Опытным путем выяснил, что это происходит, если
// при использовании @UtilityClass убрать static у методов. Вернул static и аннотацию, все заработало)
public class ItemMapper {
    public static Item itemDtoToItem(ItemDto itemDto) {
        return Item.builder().id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(itemDto.getOwnerId())
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static ItemDto itemToItemDto(Item item) {
        return ItemDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequestId())
                .build();
    }

    public static ItemResponseOnlyDto itemToItemResponseDto(Item item) {
        return ItemResponseOnlyDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
