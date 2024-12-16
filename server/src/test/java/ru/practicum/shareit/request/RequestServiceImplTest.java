package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestServiceImplTest {

    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User testUser;
    private User testUser2;
    private ItemRequest testRequest1;
    private ItemRequest testRequest2;
    private ItemRequest testRequest3;
    private Item testItem1;
    private Item testItem2;
    private Item testItem3;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);

        testUser2 = new User();
        testUser2.setName("Test User2");
        testUser2.setEmail("test2@example.com");
        testUser2 = userRepository.save(testUser2);

        testRequest1 = new ItemRequest();
        testRequest1.setDescription("Test Request 1 Description");
        testRequest1.setRequestor(testUser);
        testRequest1.setCreated(LocalDateTime.now().minusDays(1));
        testRequest1 = requestRepository.save(testRequest1);

        testRequest2 = new ItemRequest();
        testRequest2.setDescription("Test Request 2 Description");
        testRequest2.setRequestor(testUser);
        testRequest2.setCreated(LocalDateTime.now().minusHours(7));
        testRequest2 = requestRepository.save(testRequest2);

        testRequest3 = new ItemRequest();
        testRequest3.setDescription("Test Request 3 Description");
        testRequest3.setRequestor(testUser2);
        testRequest3.setCreated(LocalDateTime.now().minusHours(17));
        testRequest3 = requestRepository.save(testRequest3);

        testItem1 = new Item();
        testItem1.setName("Test Item 1");
        testItem1.setDescription("Test Item 1 Description");
        testItem1.setAvailable(true);
        testItem1.setOwnerId(testUser2.getId());
        testItem1.setRequestId(testRequest1.getId());
        testItem1 = itemRepository.save(testItem1);

        testItem2 = new Item();
        testItem2.setName("Test Item 2");
        testItem2.setDescription("Test Item 2 Description");
        testItem2.setAvailable(true);
        testItem2.setOwnerId(testUser2.getId());
        testItem2.setRequestId(testRequest3.getId());
        testItem2 = itemRepository.save(testItem2);

        testItem3 = new Item();
        testItem3.setName("Test Item 3");
        testItem3.setDescription("Test Item 3 Description");
        testItem3.setAvailable(true);
        testItem3.setOwnerId(testUser.getId());
        testItem3.setRequestId(testRequest3.getId());
        testItem3 = itemRepository.save(testItem2);
    }

    @Test
    void testSaveItemRequest() {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("New Request Description").build();

        ItemRequestDto savedRequest = requestService.saveItemRequest(testUser2.getId(), requestDto);
        assertNotNull(savedRequest.getId());
        assertEquals(testUser2.getId(), savedRequest.getRequestorId());
        assertEquals(requestDto.getDescription(), savedRequest.getDescription());
        assertNotNull(savedRequest.getCreated());
    }

    @Test
    void testGetAllRequestsByOwner() {
        List<ItemRequestDto> requests = requestService.getAllRequestsByOwner(testUser.getId());

        assertEquals(2, requests.size());
        assertEquals(testRequest1.getId(), requests.get(0).getId());
        assertEquals(testRequest2.getId(), requests.get(1).getId());

        assertEquals(1, requests.get(0).getItems().size());
        assertEquals(testItem1.getId(), requests.get(0).getItems().get(0).getId());

        assertThrows(NotFoundException.class, () -> requestService.getAllRequestsByOwner(999));
    }

    @Test
    void testGetAllRequestsOfOtherUsers() {
        List<ItemRequestDto> requests = requestService.getAllRequestsOfOtherUsers(testUser.getId());

        assertEquals(1, requests.size());
        assertEquals(testRequest3.getId(), requests.get(0).getId());
        assertEquals(testRequest3.getDescription(), requests.get(0).getDescription());
        assertEquals(testUser2.getId(), requests.get(0).getRequestorId());
        assertNotNull(requests.get(0).getCreated());

        assertEquals(1, requests.get(0).getItems().size());
        assertEquals(testItem2.getId(), requests.get(0).getItems().get(0).getId());
    }

    @Test
    void testGetItemRequest() {
        ItemRequestDto requestDto = requestService.getItemRequest(testUser.getId(), testRequest1.getId());

        assertEquals(testRequest1.getId(), requestDto.getId());
        assertEquals(testRequest1.getDescription(), requestDto.getDescription());
        assertEquals(testUser.getId(), requestDto.getRequestorId());
        assertNotNull(requestDto.getCreated());

        assertEquals(1, requestDto.getItems().size());
        assertEquals(testItem1.getId(), requestDto.getItems().get(0).getId());

        assertThrows(NotFoundException.class, () -> requestService.getItemRequest(testUser.getId(), 999));
    }

}