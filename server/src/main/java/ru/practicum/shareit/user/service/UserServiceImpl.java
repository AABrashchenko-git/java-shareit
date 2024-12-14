package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userStorage;

    @Override
    public Collection<UserDto> getAllUsers() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getEmail() == null)
            throw new ValidationException("name and email should not be null");
        validateUserDto(userDto);
        emailExists(userDto);
        User user = UserMapper.userDtoToUser(userDto);
        return UserMapper.userToUserDto(userStorage.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer userId, UserDto newUserDto) {
        validateUserDto(newUserDto);
        emailExists(newUserDto);
        User userToUpdate = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " is not found"));

        if (newUserDto.getName() != null && !newUserDto.getName().isBlank())
            userToUpdate.setName(newUserDto.getName());
        if (newUserDto.getEmail() != null && !newUserDto.getEmail().isBlank())
            userToUpdate.setEmail(newUserDto.getEmail());

        return UserMapper.userToUserDto(userToUpdate);
    }

    @Override
    public UserDto getUser(int id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " is not found"));
        return UserMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    public void removeUser(int userId) {
        userStorage.deleteById(userId);
    }

    private void validateUserDto(UserDto userDto) {
        if (userDto.getName() != null && userDto.getName().isBlank())
            throw new ValidationException("name should not be empty");
        if (userDto.getEmail() != null && userDto.getEmail().isBlank())
            throw new ValidationException("Invalid email: should not be empty");
    }

    private void emailExists(UserDto userDto) {
        boolean emailExists = !userStorage.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(userDto.getEmail()))
                .toList().isEmpty();
        if (emailExists)
            throw new DuplicatedDataException(String.format("user with email %s already exists", userDto.getEmail()));
    }

}
