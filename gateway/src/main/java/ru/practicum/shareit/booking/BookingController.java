package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("${shareit.header.owner}") Integer bookerId,
                                             @RequestBody BookingDto bookingDto) {
        log.info("POST /bookings {} is accessed by bookerId: {}", bookingDto, bookerId);
        return bookingClient.bookItem(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}") //?approved={approved}
    public ResponseEntity<Object> approveBooking(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                                 @PathVariable Integer bookingId, @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{}?approved={} is accessed", bookingId, approved);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("${shareit.header.owner}") Integer userId,
                                                 @PathVariable Integer bookingId) {
        log.info("GET /bookings/{} is accessed", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBookerId(@RequestHeader("${shareit.header.owner}") Integer bookerId,
                                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings?state={} with userId {} is accessed", state, bookerId);
        return bookingClient.getAllBookingsByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwnerId(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                                          @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings/owner?state={} with userId {} is accessed", state, ownerId);
        return bookingClient.getAllBookingsByOwnerId(ownerId, state);
    }

}
