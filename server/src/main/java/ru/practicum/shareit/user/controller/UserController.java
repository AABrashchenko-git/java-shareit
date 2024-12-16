package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("GET /users is accessed");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("POST /users is accessed");
        return userService.addUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        log.info("GET /users/{} is accessed", id);
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable int id) {
        log.info("DELETE /users/{} is accessed", id);
        userService.removeUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto updUserDto) {
        log.info("PUT /users is accessed");
        return userService.updateUser(id, updUserDto);
    }

}
