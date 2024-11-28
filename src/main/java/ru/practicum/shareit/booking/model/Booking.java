package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
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
    //@NotNull(message = "Booking Id should not be empty")
    @Column(name = "booking_id")
    private Integer id;
    //@PastOrPresent(message = "Incorrect date")
    @Column(name = "start_date")
    private LocalDateTime start;
    //@FutureOrPresent(message = "Incorrect date")
    @Column(name = "end_date")
    private LocalDateTime end;
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @ValidBookingStatus
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
