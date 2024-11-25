package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.ValidBookingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @EqualsAndHashCode(of = {"id"})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Booking Id should not be empty")
    private Integer id;
    @PastOrPresent(message = "Incorrect date")
    private LocalDateTime start;
    @FutureOrPresent(message = "Incorrect date")
    private LocalDateTime end;
    @NotNull(message = "itemId should not be empty")
    private Integer itemId;
    @NotNull(message = "bookerId should not be empty")
    private User booker;
    @ValidBookingStatus
    private BookingStatus status;
}
