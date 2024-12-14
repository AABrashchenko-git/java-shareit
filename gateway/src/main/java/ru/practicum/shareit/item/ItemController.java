package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("POST /items is accessed: {}", itemDto);
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                           @PathVariable Integer itemId, @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} is accessed: {}", itemId, itemDto);
        return itemClient.editItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("${shareit.header.owner}") Integer userId,
                                              @PathVariable Integer itemId) {
        log.info("PATCH /items/{} is accessed", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwner(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /items with ownerId {} is accessed", ownerId);
        return itemClient.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("GET /search with search query \"{}\" is accessed", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("${shareit.header.owner}") Integer authorId,
                                             @PathVariable Integer itemId, @RequestBody @Valid CommentDto commentDto) {
        log.info("POST /items/{}/comment", itemId);
        return itemClient.addComment(authorId, itemId, commentDto);
    }

}
