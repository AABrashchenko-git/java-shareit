package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class ItemDto {
    private Integer id;
    @NotBlank(message = "Item name cannot be empty")
    private String name;
    @NotBlank(message = "Item description cannot be empty")
    private String description;
    @NotNull(message = "Item availability cannot be empty")
    private Boolean available;
    private Integer ownerId;
}
