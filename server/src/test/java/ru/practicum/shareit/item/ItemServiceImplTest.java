package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private User testUser;
    private Item testItem;
    private Booking testBooking;
    private Comment testComment;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testUser = userRepository.save(testUser);

        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setAvailable(true);
        testItem.setOwnerId(testUser.getId());

        testItem = itemRepository.save(testItem);

        testBooking = new Booking();
        testBooking.setStart(LocalDateTime.now().minusDays(1));
        testBooking.setEnd(LocalDateTime.now().minusDays(2));
        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);

        testBooking = bookingRepository.save(testBooking);

        testComment = new Comment();
        testComment.setText("Test Comment");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now().minusMinutes(60));

        testComment = commentRepository.save(testComment);
    }

    @Test
    public void testAddItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("New Item")
                .description("New Description")
                .available(true).build();

        ItemDto result = itemService.addItem(testUser.getId(), itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());

        Optional<Item> savedItem = itemRepository.findById(result.getId());
        assertTrue(savedItem.isPresent());
        assertEquals(result.getName(), savedItem.get().getName());
    }

    @Test
    public void testEditItem() {
        ItemDto updatedItemDto = ItemDto.builder()
                .id(1)
                .name("Updated Name")
                .description("Updated Description")
                .available(false).build();

        ItemDto result = itemService.editItem(testUser.getId(), testItem.getId(), updatedItemDto);

        assertNotNull(result);
        assertEquals(updatedItemDto.getName(), result.getName());
        assertEquals(updatedItemDto.getDescription(), result.getDescription());
        assertEquals(updatedItemDto.getAvailable(), result.getAvailable());

        Optional<Item> updatedItem = itemRepository.findById(testItem.getId());
        assertTrue(updatedItem.isPresent());
        assertEquals(updatedItemDto.getName(), updatedItem.get().getName());
    }

    @Test
    public void testGetItemById() {
        ItemResponseOnlyDto result = itemService.getItemById(testUser.getId(), testItem.getId());

        assertNotNull(result);
        assertEquals(testItem.getName(), result.getName());
        assertEquals(testItem.getDescription(), result.getDescription());

        assertNotNull(result.getComments());
    }

    @Test
    public void testGetAllItemsByOwner() {

        List<ItemResponseOnlyDto> result = itemService.getAllItemsByOwner(testUser.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testItem.getName(), result.get(0).getName());
    }

    @Test
    public void testSearchItems() {
        List<ItemDto> result = itemService.searchItems("Test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testItem.getName(), result.get(0).getName());
    }

    @Test
    public void testAddComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("New Comment");

        CommentDto result = itemService.addComment(testUser.getId(), testItem.getId(), commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());

        Optional<Comment> savedComment = commentRepository.findById(result.getId().intValue());
        assertTrue(savedComment.isPresent());
        assertEquals(commentDto.getText(), savedComment.get().getText());
    }
}