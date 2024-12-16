package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    // Маппинг ItemDto -> Item
    Item itemDtoToItem(ItemDto itemDto);

    // Маппинг Item -> ItemDto
    ItemDto itemToItemDto(Item item);

    // Маппинг Item -> ItemResponseOnlyDto
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemResponseOnlyDto itemToItemResponseDto(Item item);
}