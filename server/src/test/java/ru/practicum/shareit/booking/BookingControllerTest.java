package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private BookingDto testBookingDto;
    private ItemDto testItemDto;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testItemDto = ItemDto.builder()
                .id(1)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        testUserDto = UserDto.builder()
                .id(1)
                .name("Test User")
                .email("test@example.com")
                .build();

        testBookingDto = BookingDto.builder()
                .id(1)
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .item(testItemDto)
                .booker(testUserDto)
                .build();
    }

    @Test
    void testAddBooking() throws Exception {
        when(bookingService.addBooking(eq(1), any(BookingDto.class)))
                .thenReturn(testBookingDto);

        BookingDto requestBookingDto = BookingDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBookingDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookingDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(testBookingDto.getItemId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(testUserDto.getId()));
    }

    @Test
    void testApproveBooking() throws Exception {
        when(bookingService.approveBooking(eq(1), eq(1), eq(true)))
                .thenReturn(testBookingDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookingDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(testUserDto.getId()));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(eq(1), eq(1)))
                .thenReturn(testBookingDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookingDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id").value(testUserDto.getId()));
    }

    @Test
    void testGetAllBookingsByBookerId() throws Exception {
        when(bookingService.getAllBookingsByBookerId(eq(1), eq(BookingState.ALL)))
                .thenReturn(List.of(testBookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testBookingDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id").value(testUserDto.getId()));
    }

    @Test
    void testGetAllBookingsByOwnerId() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(eq(1), eq(BookingState.ALL)))
                .thenReturn(List.of(testBookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testBookingDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id").value(testUserDto.getId()));
    }
}