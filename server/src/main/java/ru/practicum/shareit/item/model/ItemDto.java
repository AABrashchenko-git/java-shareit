package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer ownerId;
    private Integer requestId;
}
