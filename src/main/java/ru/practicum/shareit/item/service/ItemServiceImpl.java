package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        validateItemDto(itemDto);
        userStorage.findById(ownerId).orElseThrow(() -> new NotFoundException("User " + ownerId + " is not found"));
        Item item = ItemMapper.itemDtoToItem(itemDto);
        item.setOwnerId(ownerId);
        return ItemMapper.itemToItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto editItem(Integer ownerId, Integer itemId, ItemDto itemDto) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " is not found"));

        if (!item.getOwnerId().equals(ownerId))
            throw new AccessDeniedException(String.format("User %d has no access to modify the resource", ownerId));

        if (itemDto.getName() != null && !itemDto.getName().isBlank())
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank())
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());

        return ItemMapper.itemToItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto getItemById(Integer itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " is not found"));
        return ItemMapper.itemToItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemStorage.findAllByOwnerId(ownerId).stream().map(ItemMapper::itemToItemDto).toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.findItemsBySearchQuery(text)
                .stream().map(ItemMapper::itemToItemDto)
                .filter(ItemDto::getAvailable).toList();
    }

    @Override
    public CommentDto addComment(Integer itemId, CommentDto commentDto) {
        //TODO
        return null;
    }

    private void validateItemDto(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank())
            throw new ValidationException("name should not be empty");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank())
            throw new ValidationException("Invalid Description: should not be empty");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("Availability: should not be empty");
    }

}
