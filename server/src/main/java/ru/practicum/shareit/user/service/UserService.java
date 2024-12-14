package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getAllUsers();

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Integer userId, UserDto newUserDto);

    UserDto getUser(int id);

    void removeUser(int userId);
}
