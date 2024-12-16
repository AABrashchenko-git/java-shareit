package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;
    private User testUser;
    private Item testItem;
    private User testUser2;
    private User testUser3;
    private Item testItem2;
    private Item testItem3;
    private Booking testBooking;
    private Booking testBooking2;
    private Booking testBooking3;

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

        testUser3 = new User();
        testUser3.setName("Test User3");
        testUser3.setEmail("test3@example.com");
        testUser3 = userRepository.save(testUser3);

        testItem = new Item();
        testItem.setName("Test Item2");
        testItem.setDescription("Test Description2");
        testItem.setAvailable(true);
        testItem.setOwnerId(testUser.getId());
        testItem = itemRepository.save(testItem);

        testItem2 = new Item();
        testItem2.setName("Test Item");
        testItem2.setDescription("Test Description2");
        testItem2.setAvailable(true);
        testItem2.setOwnerId(testUser2.getId());
        testItem2 = itemRepository.save(testItem2);

        testItem3 = new Item();
        testItem3.setName("Test Item3");
        testItem3.setDescription("Test Description3");
        testItem3.setAvailable(true);
        testItem3.setOwnerId(testUser2.getId());
        testItem3 = itemRepository.save(testItem3);

        testBooking = new Booking();
        testBooking.setStart(LocalDateTime.now().plusDays(1));
        testBooking.setEnd(LocalDateTime.now().plusDays(2));
        testBooking.setItem(testItem2);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);
        testBooking = bookingRepository.save(testBooking);

        testBooking2 = new Booking();
        testBooking2.setStart(LocalDateTime.now().plusDays(3));
        testBooking2.setEnd(LocalDateTime.now().plusDays(4));
        testBooking2.setItem(testItem);
        testBooking2.setBooker(testUser2);
        testBooking2.setStatus(BookingStatus.WAITING);
        testBooking2 = bookingRepository.save(testBooking2);

        testBooking3 = new Booking();
        testBooking3.setStart(LocalDateTime.now().minusDays(1));
        testBooking3.setEnd(LocalDateTime.now().minusDays(2));
        testBooking3.setItem(testItem3);
        testBooking3.setBooker(testUser);
        testBooking3.setStatus(BookingStatus.WAITING);
        testBooking3 = bookingRepository.save(testBooking3);
    }

    @Test
    void testAddBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(testItem.getId())
                .start(LocalDateTime.now().plusDays(4))
                .end(LocalDateTime.now().plusDays(5)).build();

        BookingDto result = bookingService.addBooking(testUser2.getId(), bookingDto);
        assertNotNull(result);
        assertEquals(bookingDto.getItemId(), result.getItemId());
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(result.getStatus(), BookingStatus.WAITING);
        assertEquals(testUser2.getId(), result.getBooker().getId());
        System.out.println("Existing bookings: " + bookingRepository.findAll());
        Optional<Booking> savedBooking = bookingRepository.findById(result.getId());
        assertTrue(savedBooking.isPresent());
        assertEquals(4, result.getId());
    }

    @Test
    void testApproveBooking() {
        BookingDto approvedBooking = bookingService.approveBooking(testUser2.getId(), testBooking.getId(), true);
        assertEquals(approvedBooking.getStatus(), BookingStatus.APPROVED);
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () ->
                bookingService.approveBooking(testUser3.getId(), testBooking.getId(), true));
        assertEquals(ex.getMessage(), "approval denied: user is not the owner of the item to book");
    }

    @Test
    void testGetBookingById() {
        BookingDto result = bookingService.getBookingById(testUser.getId(), testBooking.getId());
        assertEquals(testBooking.getItem().getId(), result.getItem().getId());
        assertThrows(InvalidRequestException.class, () ->
                bookingService.getBookingById(testUser3.getId(), testBooking.getId()));
    }

    @Test
    void testGetAllBookingsByBookerId() {
        List<BookingDto> fromDataBase = bookingService.getAllBookingsByBookerId(testUser.getId(), BookingState.ALL);
        List<BookingDto> existing = Stream.of(testBooking3, testBooking)
                .map(bookingMapper::toBookingDto).toList();
        assertEquals(existing, fromDataBase);
    }

    @Test
    void testGetAllBookingsByOwnerId() {
        List<BookingDto> fromDataBase = bookingService.getAllBookingsByOwnerId(testUser2.getId(), BookingState.ALL);
        List<BookingDto> existing = Stream.of(testBooking3, testBooking)
                .map(bookingMapper::toBookingDto).toList();
        assertEquals(existing, fromDataBase);
    }
}