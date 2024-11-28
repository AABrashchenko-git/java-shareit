package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Optional<User> getUser(Integer userId);

    User addUser(User user);

    User updateUser(User updUser);

    void removeUser(Integer id);
}
