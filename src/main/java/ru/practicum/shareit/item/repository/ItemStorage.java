package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item editItem(Item item);

    Item getItemById(Integer itemId);

    List<Item> getAllItemsByOwner(Integer ownerId);

    List<Item> searchItems(String text);
}
