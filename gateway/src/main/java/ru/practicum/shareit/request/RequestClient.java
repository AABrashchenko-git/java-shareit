package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> getAllRequestsByOwner(Integer ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> getAllRequestsOfOtherUsers(Integer ownerId) {
        return get("/all", ownerId);
    }

    public ResponseEntity<Object> getItemRequest(Integer ownerId, Integer requestId) {
        return get("/" + requestId, ownerId);
    }

    public ResponseEntity<Object> saveItemRequest(Integer requestorId, ItemRequestDto itemRequestDto) {
        return post("", requestorId, itemRequestDto);
    }

}