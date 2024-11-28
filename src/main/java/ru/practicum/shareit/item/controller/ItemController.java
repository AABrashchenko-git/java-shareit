package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                           @RequestBody @Valid ItemDto itemDto) {
        log.info("POST /items is accessed: {}", itemDto);
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                            @PathVariable Integer itemId, @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} is accessed: {}", itemId, itemDto);
        return itemService.editItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseOnlyDto getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable Integer itemId) {
        log.info("PATCH /items/{} is accessed", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemResponseOnlyDto> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("GET /items with ownerId {} is accessed", ownerId);
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("GET /search with search query \"{}\" is accessed", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Integer authorId,
                                 @PathVariable Integer itemId, @RequestBody @Valid CommentDto commentDto) {
        log.info("POST /items/{}/comment", itemId);
        return itemService.addComment(authorId, itemId, commentDto);
    }
}
