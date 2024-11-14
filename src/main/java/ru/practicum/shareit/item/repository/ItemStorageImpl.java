package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        log.info("post /items with itemId = {} handled", item);
        return item;
    }

    @Override
    public Item editItem(Item item) {
        items.replace(item.getId(), item);
        log.info("patch /items/{} handled", item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Integer itemId) {
        log.info("get /items/{} handled", itemId);
        return Optional.of(items.get(itemId));
    }

    @Override
    public List<Item> getAllItemsByOwner(Integer ownerId) {
        log.info("get /items for owner {} handled", ownerId);
        return items.values().stream().filter(i -> i.getOwnerId().equals(ownerId)).toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        log.info("search /items/search?text={} handled", text);
        if (text.isBlank())
            return Collections.emptyList();
        return items.values().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        i.getName().toLowerCase().contains(text.toLowerCase())).toList();
    }

    private Integer getNextId() {
        Integer currentMaxId = items.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
