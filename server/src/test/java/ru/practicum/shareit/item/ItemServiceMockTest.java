package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceMockTest {

    @Mock
    private ItemRepository itemStorage;

    @Mock
    private UserRepository userStorage;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User testUser;
    private Item testItem;
    private Booking testBooking;
    private Comment testComment;
    private ItemDto testItemDto;
    private CommentDto testCommentDto;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testItem = new Item();
        testItem.setId(1);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setAvailable(true);
        testItem.setOwnerId(testUser.getId());

        testBooking = new Booking();
        testBooking.setId(1);
        testBooking.setStart(LocalDateTime.now().minusDays(1));
        testBooking.setEnd(LocalDateTime.now().minusDays(2));
        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.APPROVED);

        testComment = new Comment();
        testComment.setId(1);
        testComment.setText("Test Comment");
        testComment.setItem(testItem);
        testComment.setAuthor(testUser);
        testComment.setCreated(LocalDateTime.now().minusMinutes(60));

        testItemDto = ItemDto.builder()
                .id(testItem.getId())
                .name(testItem.getName())
                .description(testItem.getDescription())
                .available(testItem.getAvailable())
                .build();

        testCommentDto = new CommentDto();
        testCommentDto.setId(Long.valueOf(testComment.getId()));
        testCommentDto.setText(testComment.getText());
    }

    @Test
    public void testAddItem() {
        when(userStorage.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(itemStorage.save(any(Item.class))).thenReturn(testItem);

        ItemDto result = itemService.addItem(testUser.getId(), testItemDto);

        assertNotNull(result);
        assertEquals(testItemDto.getName(), result.getName());
        assertEquals(testItemDto.getDescription(), result.getDescription());
        assertEquals(testItemDto.getAvailable(), result.getAvailable());

        verify(itemStorage, times(1)).save(any(Item.class));
    }

    @Test
    public void testEditItem() {
        when(itemStorage.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        ItemDto updatedItemDto = ItemDto.builder()
                .id(testItem.getId())
                .name("Updated Name")
                .description("Updated Description")
                .available(false).build();

        ItemDto result = itemService.editItem(testUser.getId(), testItem.getId(), updatedItemDto);

        assertNotNull(result);
        assertEquals(updatedItemDto.getName(), result.getName());
        assertEquals(updatedItemDto.getDescription(), result.getDescription());
        assertEquals(updatedItemDto.getAvailable(), result.getAvailable());

        verify(itemStorage, never()).save(any(Item.class));
    }

    @Test
    public void testGetItemById() {
        when(itemStorage.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(commentRepository.findAllByItemId(testItem.getId())).thenReturn(List.of(testComment));
        when(commentMapper.toCommentDto(testComment)).thenReturn(testCommentDto);

        ItemResponseOnlyDto result = itemService.getItemById(testUser.getId(), testItem.getId());

        assertNotNull(result);
        assertEquals(testItem.getName(), result.getName());
        assertEquals(testItem.getDescription(), result.getDescription());
        assertNotNull(result.getComments());
        assertEquals(1, result.getComments().size());
    }

    @Test
    public void testGetAllItemsByOwner() {
        when(itemStorage.findAllByOwnerId(testUser.getId())).thenReturn(List.of(testItem));
        when(commentRepository.findAllByItemId(testItem.getId())).thenReturn(List.of(testComment));
        when(commentMapper.toCommentDto(testComment)).thenReturn(testCommentDto);

        List<ItemResponseOnlyDto> result = itemService.getAllItemsByOwner(testUser.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testItem.getName(), result.get(0).getName());
        assertNotNull(result.get(0).getComments());
        assertEquals(1, result.get(0).getComments().size());
    }

    @Test
    public void testSearchItems() {
        when(itemStorage.findItemsBySearchQuery("Test")).thenReturn(List.of(testItem));

        List<ItemDto> result = itemService.searchItems("Test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testItem.getName(), result.get(0).getName());
    }

    @Test
    public void testAddComment() {
        when(bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(
                eq(testItem.getId()), eq(testUser.getId()), any(LocalDateTime.class)))
                .thenReturn(List.of(testBooking));

        when(userStorage.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(itemStorage.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toCommentDto(testComment)).thenReturn(testCommentDto);
        when(commentMapper.toComment(testCommentDto)).thenReturn(testComment);

        CommentDto result = itemService.addComment(testUser.getId(), testItem.getId(), testCommentDto);

        assertNotNull(result);
        assertEquals(testCommentDto.getText(), result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}