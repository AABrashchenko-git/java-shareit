package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validator.ValidBookingStatus;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
public class Booking {
    @NotNull(message = "Booking Id should not be empty")
    private Integer id;
    @PastOrPresent(message = "Incorrect date")
    private LocalDateTime start;
    @FutureOrPresent(message = "Incorrect date")
    private LocalDateTime end;
    @NotNull(message = "itemId should not be empty")
    private Integer itemId;
    @NotNull(message = "bookerId should not be empty")
    private Integer bookerId;
    @ValidBookingStatus
    private BookingStatus status;
}
