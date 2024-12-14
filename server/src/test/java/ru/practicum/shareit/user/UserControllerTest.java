package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto testUserDto;
    private UserDto testUserDto2;

    @BeforeEach
    void setUp() {
        testUserDto = UserDto.builder()
                .id(1)
                .name("Test User")
                .email("test@example.com")
                .build();

        testUserDto2 = UserDto.builder()
                .id(2)
                .name("Test User2")
                .email("test2@example.com")
                .build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUserDto, testUserDto2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testUserDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(testUserDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(testUserDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(testUserDto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(testUserDto2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(testUserDto2.getEmail()));
    }

    @Test
    void testAddUser() throws Exception {
        when(userService.addUser(any(UserDto.class))).thenReturn(testUserDto);

        UserDto requestUserDto = UserDto.builder()
                .name("test User")
                .email("test@example.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUserDto.getEmail()));
    }

    @Test
    void testGetUser() throws Exception {
        when(userService.getUser(eq(1))).thenReturn(testUserDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUserDto.getEmail()));
    }

    @Test
    void testRemoveUser() throws Exception {
        doNothing().when(userService).removeUser(eq(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(eq(1), any(UserDto.class))).thenReturn(testUserDto);

        UserDto requestUserDto = UserDto.builder()
                .name("Updated User")
                .email("updated@example.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testUserDto.getEmail()));
    }
}