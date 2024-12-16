package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    private ItemRequestDto testRequestDto;

    @BeforeEach
    void setUp() {
        testRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("Test Request Description")
                .requestorId(1)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testSaveItemRequest() throws Exception {
        when(requestService.saveItemRequest(eq(1), any(ItemRequestDto.class)))
                .thenReturn(testRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testRequestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testRequestDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId").value(testRequestDto.getRequestorId()));
    }

    @Test
    void testGetAllRequestsByOwner() throws Exception {
        when(requestService.getAllRequestsByOwner(eq(1)))
                .thenReturn(List.of(testRequestDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testRequestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value(testRequestDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId")
                        .value(testRequestDto.getRequestorId()));
    }

    @Test
    void testGetAllRequestsOfOtherUsers() throws Exception {
        when(requestService.getAllRequestsOfOtherUsers(eq(1)))
                .thenReturn(List.of(testRequestDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(testRequestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                        .value(testRequestDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestorId")
                        .value(testRequestDto.getRequestorId()));
    }

    @Test
    void testGetItemRequest() throws Exception {
        when(requestService.getItemRequest(eq(1), eq(1)))
                .thenReturn(testRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(testRequestDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(testRequestDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestorId")
                        .value(testRequestDto.getRequestorId()));
    }
}