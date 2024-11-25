package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "items")
@Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(of = {"id"})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "description cannot be empty")
    private String description;
    @NotNull(message = "availability should not be empty")
    private Boolean available;
    @NotNull(message = "ownerId should not be empty")
    private Integer ownerId;
}
