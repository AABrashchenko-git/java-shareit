package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto editItem(Integer ownerId, Integer itemId, ItemDto itemDto);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> getAllItemsByOwner(Integer ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Integer itemId, CommentDto commentDto);
}
