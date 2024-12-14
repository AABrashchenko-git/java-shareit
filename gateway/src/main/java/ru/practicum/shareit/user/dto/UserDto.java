package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class UserDto {
    private Integer id;
    @Email(message = "Invalid Email format, expected nickname@postservice.com")
    private String email;
    private String name;
}