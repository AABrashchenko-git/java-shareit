package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("${shareit.header.owner}") Integer bookerId,
                                 @RequestBody BookingDto bookingDto) {
        log.info("POST /bookings {} is accessed by bookerId: {}", bookingDto, bookerId);
        return bookingService.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}") //?approved={approved}
    public BookingDto approveBooking(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                     @PathVariable Integer bookingId, @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{}?approved={} is accessed", bookingId, approved);
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBookingById(@RequestHeader("${shareit.header.owner}") Integer userId,
                                     @PathVariable Integer bookingId) {
        log.info("GET /bookings/{} is accessed", bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerId(@RequestHeader("${shareit.header.owner}") Integer bookerId,
                                                     @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings?state={} with userId {} is accessed", state, bookerId);
        return bookingService.getAllBookingsByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByOwnerId(@RequestHeader("${shareit.header.owner}") Integer ownerId,
                                                    @RequestParam BookingState state) {
        log.info("GET /bookings/owner?state={} with userId {} is accessed", state, ownerId);
        return bookingService.getAllBookingsByOwnerId(ownerId, state);
    }
}
