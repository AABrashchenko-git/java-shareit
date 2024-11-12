package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer ownerId;
}
