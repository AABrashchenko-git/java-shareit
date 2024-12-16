package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotBlank(message = "name should not be empty")
    private String name;
    @NotBlank(message = "description should not be empty")
    private String description;
    @NotNull(message = "availability should not be empty")
    private Boolean available;
    @JsonIgnore
    private Integer ownerId;
    private Integer requestId;
}

