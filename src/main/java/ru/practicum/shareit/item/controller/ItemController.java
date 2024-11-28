package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                           @RequestBody @Valid ItemDto itemDto) {
        log.info("POST /items is accessed: {}", itemDto);
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                            @PathVariable Integer itemId, @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} is accessed: {}", itemId, itemDto);
        return itemService.editItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Integer itemId) {
        log.info("PATCH /items/{} is accessed", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /items with ownerId {} is accessed", ownerId);
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("GET /search with search query \"{}\" is accessed", text);
        return itemService.searchItems(text);
    }
}
