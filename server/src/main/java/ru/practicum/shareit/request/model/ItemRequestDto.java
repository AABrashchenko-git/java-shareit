package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class ItemRequestDto {
    private Integer id;
    private String description;
    private Integer requestorId;
    private LocalDateTime created;
    private List<ItemDto> items;
}
