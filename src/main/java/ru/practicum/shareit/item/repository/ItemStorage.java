package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item addItem(Item item);

    Item editItem(Item item);

    Optional<Item> getItemById(Integer itemId);

    List<Item> getAllItemsByOwner(Integer ownerId);

    List<Item> searchItems(String text);
}
