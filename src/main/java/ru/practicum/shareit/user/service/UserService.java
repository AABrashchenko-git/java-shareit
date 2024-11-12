package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        emailExists(userDto);
        User user = UserMapper.userDtoToUser(userDto);
        return UserMapper.userToUserDto(userStorage.addUser(user));
    }

    public UserDto updateUser(Integer userId, UserDto newUserDto) {
        emailExists(newUserDto);
        User userToUpdate = userStorage.getUser(userId);
        if (userToUpdate == null)
            throw new NotFoundException("User " + userId + " is not found");
        if (newUserDto.getName() != null)
            userToUpdate.setName(newUserDto.getName());
        if (newUserDto.getEmail() != null)
            userToUpdate.setEmail(newUserDto.getEmail());

        return UserMapper.userToUserDto(userStorage.updateUser(userToUpdate));
    }

    public UserDto getUser(int id) {
        User user = userStorage.getUser(id);
        return UserMapper.userToUserDto(user);
    }

    public void removeUser(int userId) {
        userStorage.removeUser(userId);
    }

    private void emailExists(UserDto userDto) {
        boolean emailExists = !userStorage.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(userDto.getEmail()))
                .toList().isEmpty();
        if (emailExists)
            throw new DuplicatedDataException(String.format("user with email %s already exists", userDto.getEmail()));
    }

}
