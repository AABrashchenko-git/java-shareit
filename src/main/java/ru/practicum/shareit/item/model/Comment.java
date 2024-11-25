package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(of = {"id"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Item item;
    private User author;
    private final LocalDateTime created = LocalDateTime.now();
}
