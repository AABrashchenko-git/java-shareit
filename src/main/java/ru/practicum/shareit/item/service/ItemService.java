package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto editItem(Integer ownerId, Integer itemId, ItemDto itemDto);

    ItemResponseOnlyDto getItemById(Integer userId, Integer itemId);

    List<ItemResponseOnlyDto> getAllItemsByOwner(Integer ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Integer authorId, Integer itemId, CommentDto commentDto);
}
