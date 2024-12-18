package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User userDtoToUser(UserDto userDto) {
        return User.builder().id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto userToUserDto(User user) {
        return UserDto.builder().id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

}
