package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class User {
    private Integer id;
    @Email(message = "Invalid Email format, expected nickname@postservice.com")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Login cannot be empty")
    private String name;
}

