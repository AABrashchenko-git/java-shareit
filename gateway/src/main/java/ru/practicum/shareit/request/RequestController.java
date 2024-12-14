package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByOwner(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /requests with ownerId {} is accessed", ownerId);
        return requestClient.getAllRequestsByOwner(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOfOtherUsers(@RequestHeader("${shareit.header.owner}") Integer ownerId) {
        log.info("GET /requests/all with ownerId {} is accessed", ownerId);
        return requestClient.getAllRequestsOfOtherUsers(ownerId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                                 @PathVariable Integer requestId) {
        log.info("GET /items/{} is accessed", requestId);
        return requestClient.getItemRequest(ownerId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItemRequest(@RequestHeader("${shareit.header.owner}") Integer requestorId,
                                                  @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("POST /requests is accessed: {}, owner Id  {}", itemRequestDto, requestorId);
        return requestClient.saveItemRequest(requestorId, itemRequestDto);
    }

}
