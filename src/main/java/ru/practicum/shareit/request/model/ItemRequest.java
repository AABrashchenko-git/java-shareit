package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(of = {"id"})
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "description cannot be empty")
    private String description;
    @NotNull(message = "requestorId should not be empty")
    private User requestor;
    @PastOrPresent
    private LocalDateTime created;
}
