package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ItemRequestDto> getAllRequestsByOwner(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /requests with ownerId {} is accessed", ownerId);
        return requestService.getAllRequestsByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsOfOtherUsers(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /requests/all with ownerId {} is accessed", ownerId);
        return requestService.getAllRequestsOfOtherUsers(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("${shareit.header.owner}") Integer ownerId, @PathVariable Integer requestId) {
        log.info("GET /items/{} is accessed", requestId);
        return requestService.getItemRequest(ownerId, requestId);
    }

    @PostMapping
    public ItemRequestDto saveItemRequest(@RequestHeader("${shareit.header.owner}") Integer requestorId,
                                          @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST /requests is accessed: {}, owner Id  {}", itemRequestDto, requestorId);
        return requestService.saveItemRequest(requestorId, itemRequestDto);
    }


}
