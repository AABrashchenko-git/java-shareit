package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class ItemRequest {
    private Integer id;
    @NotBlank(message = "description cannot be empty")
    private String description;
    @NotNull(message = "requestorId should not be empty")
    private Integer requestorId;
    @PastOrPresent
    private LocalDateTime created;
}
