package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemResponseOnlyDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemDto testItemDto;
    private ItemDto testItemDto2;
    private ItemDto testItemDto3;
    private UserDto testUserDto;
    private UserDto testUserDto2;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testUserDto = UserDto.builder()
                .id(1)
                .name("test User")
                .email("test@example.com")
                .build();
        testUserDto2 = UserDto.builder()
                .id(2)
                .name("test User2")
                .email("test2@example.com")
                .build();

        testItemDto = ItemDto.builder()
                .id(1)
                .name("test Item")
                .description("test Description")
                .available(true)
                .ownerId(testUserDto.getId()).build();
        testItemDto2 = ItemDto.builder()
                .id(2)
                .name("test Item2")
                .description("test Description2")
                .available(true)
                .ownerId(testUserDto2.getId()).build();
        testItemDto3 = ItemDto.builder()
                .id(3)
                .name("test Item3")
                .description("test Description3")
                .available(true)
                .ownerId(testUserDto.getId()).build();

        testCommentDto = new CommentDto();
        testCommentDto.setId(1L);
        testCommentDto.setText("New Comment");
        testCommentDto.setAuthorName("author Name");
        testCommentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.addItem(eq(1), any(ItemDto.class)))
                .thenReturn(testItemDto);

        ItemDto requestItemDto = ItemDto.builder()
                .name("test Item")
                .description("test Description")
                .available(true).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testItemDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testItemDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(testItemDto.getOwnerId()));
    }

    @Test
    void testEditItem() throws Exception {
        ItemDto updatedItemDto = ItemDto.builder()
                .id(1)
                .name("Updated Name")
                .description("Updated Description")
                .available(false).build();
        when(itemService.editItem(eq(testUserDto.getId()), eq(testItemDto.getId()), any(ItemDto.class)))
                .thenReturn(updatedItemDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItemDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedItemDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath(("$.description")).value(updatedItemDto.getDescription()));
    }

    @Test
    void testGetItemById() throws Exception {
        ItemResponseOnlyDto response = dtoToResponseDtoForTest(testItemDto);
        when(itemService.getItemById(eq(testUserDto.getId()), eq(testItemDto.getId())))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testItemDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testItemDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testItemDto.getDescription()));
    }

    @Test
    void testGetAllItemsByOwner() throws Exception {
        List<ItemResponseOnlyDto> responseList = Stream.of(testItemDto, testItemDto3)
                .map(this::dtoToResponseDtoForTest).toList();
        when(itemService.getAllItemsByOwner(1)).thenReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(responseList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(responseList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(responseList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(responseList.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(responseList.get(1).getName()));
    }

    @Test
    void testSearchItems() throws Exception {
        List<ItemDto> responseList = List.of(testItemDto, testItemDto2, testItemDto3);

        when(itemService.searchItems(matches(".*test.*")))
                .thenReturn(responseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "testQuery"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(responseList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(responseList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(responseList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(responseList.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(responseList.get(1).getName()));
    }

    @Test
    void testAddComment() throws Exception {
        when(itemService.addComment(eq(2), eq(1), any(CommentDto.class)))
                .thenReturn(testCommentDto);

        ItemDto requestItemDto = ItemDto.builder()
                .name("test Item")
                .description("test Description")
                .available(true).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testCommentDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value(testCommentDto.getAuthorName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(testCommentDto.getText()));

    }

    private ItemResponseOnlyDto dtoToResponseDtoForTest(ItemDto itemDto) {
        return ItemMapper.itemToItemResponseDto(ItemMapper.itemDtoToItem(itemDto));
    }
}