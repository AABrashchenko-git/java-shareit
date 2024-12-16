package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class UserDto {
    private Integer id;
    @NotEmpty(message = "Invalid Email format, expected nickname@postservice.com")
    @Email(message = "Invalid Email format, expected nickname@postservice.com")
    private String email;
    @NotBlank(message = "Invalid name format, should not be empty")
    private String name;
}