package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    private final UserService userService;
    private final UserRepository userRepository;

    private UserDto testUser1;
    private UserDto testUser2;
    private UserDto testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = UserDto.builder()
                .name("Test User 1")
                .email("test1@example.com")
                .build();

        testUser2 = UserDto.builder()
                .name("Test User 2")
                .email("test2@example.com")
                .build();

        testUser3 = UserDto.builder()
                .name("Test User 3")
                .email("test3@example.com")
                .build();

        testUser1 = userService.addUser(testUser1);
        testUser2 = userService.addUser(testUser2);
        testUser3 = userService.addUser(testUser3);
    }

    @Test
    void testAddUser() {
        UserDto newUser = UserDto.builder()
                .name("New User")
                .email("new@example.com")
                .build();

        UserDto savedUser = userService.addUser(newUser);

        assertNotNull(savedUser.getId());
        assertEquals(newUser.getName(), savedUser.getName());
        assertEquals(newUser.getEmail(), savedUser.getEmail());

        User userFromDb = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(savedUser.getName(), userFromDb.getName());
        assertEquals(savedUser.getEmail(), userFromDb.getEmail());
    }

    @Test
    void testAddUserInvalidEmail() {
        UserDto invalidUser = UserDto.builder()
                .name("Invalid User")
                .email("")
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.addUser(invalidUser));
        assertEquals("Invalid email: should not be empty", exception.getMessage());
    }

    @Test
    void testAddUserDuplicateEmail() {
        UserDto duplicateUser = UserDto.builder()
                .name("Duplicate User")
                .email(testUser1.getEmail())
                .build();

        DuplicatedDataException exception =
                assertThrows(DuplicatedDataException.class, () -> userService.addUser(duplicateUser));
        assertEquals("user with email " + testUser1.getEmail() + " already exists", exception.getMessage());
    }

    @Test
    void testGetAllUsers() {
        Collection<UserDto> users = userService.getAllUsers();

        assertEquals(3, users.size());
        assertTrue(users.contains(testUser1));
        assertTrue(users.contains(testUser2));
        assertTrue(users.contains(testUser3));
    }

    @Test
    void testGetUser() {
        UserDto user = userService.getUser(testUser1.getId());

        assertEquals(testUser1.getId(), user.getId());
        assertEquals(testUser1.getName(), user.getName());
        assertEquals(testUser1.getEmail(), user.getEmail());
    }

    @Test
    void testGetUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getUser(999));
    }

    @Test
    void testUpdateUser() {
        UserDto updatedUser = UserDto.builder()
                .name("Updated User")
                .email("updated@example.com")
                .build();

        UserDto result = userService.updateUser(testUser1.getId(), updatedUser);

        assertEquals(testUser1.getId(), result.getId());
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());

        User userFromDb = userRepository.findById(testUser1.getId()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(updatedUser.getName(), userFromDb.getName());
        assertEquals(updatedUser.getEmail(), userFromDb.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        UserDto updatedUser = UserDto.builder()
                .name("Updated User")
                .email("updated@example.com")
                .build();

        assertThrows(NotFoundException.class, () -> userService.updateUser(999, updatedUser));
    }

    @Test
    void testRemoveUser() {
        userService.removeUser(testUser1.getId());
        assertFalse(userRepository.existsById(testUser1.getId()));
    }
}