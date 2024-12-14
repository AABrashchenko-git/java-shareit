package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceMockTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private User testUser2;
    private Item testItem;
    private Booking testBooking;
    private BookingDto testBookingDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testUser2 = new User();
        testUser2.setId(2);
        testUser2.setName("Test User2");
        testUser2.setEmail("tes2t@example.com");

        testItem = new Item();
        testItem.setId(1);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setAvailable(true);
        testItem.setOwnerId(testUser.getId());

        testBooking = new Booking();
        testBooking.setId(1);
        testBooking.setStart(LocalDateTime.now().plusDays(1));
        testBooking.setEnd(LocalDateTime.now().plusDays(2));
        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);

        testBookingDto = BookingDto.builder()
                .id(testBooking.getId())
                .itemId(testItem.getId())
                .start(testBooking.getStart())
                .end(testBooking.getEnd())
                .status(BookingStatus.APPROVED)
                .item(ItemMapper.itemToItemDto(testItem))
                .booker(UserMapper.userToUserDto(testUser2))
                .build();
    }

    @Test
    void testAddBooking() {
        // Настройка моков
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
//        when(userRepository.existsById(testUser.getId())).thenReturn(true);
//        when(userRepository.existsById(testUser2.getId())).thenReturn(true);
//        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.findById(testUser2.getId())).thenReturn(Optional.of(testUser2));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);
        when(bookingMapper.toBooking(testBookingDto)).thenReturn(testBooking);

        // Вызов тестируемого метода
        BookingDto result = bookingService.addBooking(testUser2.getId(), testBookingDto);

        // Проверка результата
        assertNotNull(result);
        assertEquals(testBookingDto.getItemId(), result.getItemId());
        assertEquals(testBookingDto.getStart(), result.getStart());
        assertEquals(testUser2.getId(), result.getBooker().getId());
    }

    @Test
    void testApproveBooking() {
        // Настройка моков
        when(bookingRepository.findById(testBooking.getId())).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(testUser.getId())).thenReturn(true); // Проверка существования пользователя
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        // Вызов тестируемого метода
        BookingDto approvedBooking = bookingService.approveBooking(testUser.getId(), testBooking.getId(), true);
        ValidationException ex = assertThrows(ValidationException.class, () ->
                bookingService.approveBooking(2, testBooking.getId(), true));
        assertEquals("user not found", ex.getMessage());
    }

    @Test
    void testGetBookingById() {
        // Настройка моков
        when(bookingRepository.findById(testBooking.getId())).thenReturn(Optional.of(testBooking));
        when(userRepository.existsById(testUser.getId())).thenReturn(true); // Проверка существования пользователя
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        // Вызов тестируемого метода
        BookingDto result = bookingService.getBookingById(testUser.getId(), testBooking.getId());

        // Проверка результата
        assertEquals(testBooking.getItem().getId(), result.getItem().getId());

        // Проверка исключения
        assertThrows(ValidationException.class, () ->
                bookingService.getBookingById(2, testBooking.getId()));
    }

    @Test
    void testGetAllBookingsByBookerId() {
        // Настройка моков
        when(bookingRepository.findAllByBookerId(testUser.getId())).thenReturn(List.of(testBooking));
        when(userRepository.existsById(testUser.getId())).thenReturn(true); // Проверка существования пользователя
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        // Вызов тестируемого метода
        List<BookingDto> fromDataBase = bookingService.getAllBookingsByBookerId(testUser.getId(), BookingState.ALL);

        // Проверка результата
        assertEquals(List.of(testBookingDto), fromDataBase);
    }

}