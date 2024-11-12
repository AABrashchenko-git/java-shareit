package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        if (userStorage.getUser(ownerId) == null)
            throw new NotFoundException("User " + ownerId + " is not found");
        Item item = ItemMapper.itemDtoToItem(itemDto);
        item.setOwnerId(ownerId);
        return ItemMapper.itemToItemDto(itemStorage.addItem(item));
    }

    public ItemDto editItem(Integer ownerId, Integer itemId, ItemDto itemDto) {
        Item item = itemStorage.getItemById(itemId);
        if (item == null)
            throw new NotFoundException("Item " + itemId + " is not found");
        if (!item.getOwnerId().equals(ownerId))
            throw new AccessDeniedException(String.format("User %d has no access to modify the resource", ownerId));

        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        return ItemMapper.itemToItemDto(itemStorage.editItem(item));
    }

    public ItemDto getItemById(Integer itemId) {
        Item item = itemStorage.getItemById(itemId);
        if (item == null)
            throw new NotFoundException("Item " + itemId + " is not found");
        return ItemMapper.itemToItemDto(item);
    }

    public List<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemStorage.getAllItemsByOwner(ownerId).stream().map(ItemMapper::itemToItemDto).toList();
    }

    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text)
                .stream().map(ItemMapper::itemToItemDto)
                .filter(ItemDto::getAvailable).toList();
    }

}
