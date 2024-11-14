package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        log.info("/users get all users handled");
        return users.values();
    }

    @Override
    public Optional<User> getUser(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("user with id = %d is not found", userId));
        }
        log.info("get /users/{} handled", userId);
        return Optional.of(users.get(userId));
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("User {} is created", user.getName());
        return user;
    }

    @Override
    public User updateUser(User updUser) {
        if (users.containsKey(updUser.getId())) {
            users.replace(updUser.getId(), updUser);
            log.info("User {} is updated: {}", updUser.getName(), updUser);
        } else {
            throw new NotFoundException("User is not found",
                    new Throwable("User ID: %d ;" + updUser.getId()).fillInStackTrace());
        }
        return updUser;
    }

    @Override
    public void removeUser(Integer userId) {
        if (users.get(userId) != null) {
            users.remove(userId);
            log.info("User with id = {} is removed", userId);
        } else {
            throw new NotFoundException("User is not found",
                    new Throwable("User ID: %d ;" + userId).fillInStackTrace());
        }
    }

    private Integer getNextId() {
        Integer currentMaxId = users.keySet()
                .stream()
                .max(Integer::compare)
                .orElse(0);
        return ++currentMaxId;
    }
}
