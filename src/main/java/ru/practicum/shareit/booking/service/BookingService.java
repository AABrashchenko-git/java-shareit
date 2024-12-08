package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(Integer bookerId, BookingDto bookingDto);

    BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved);

    BookingDto getBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getAllBookingsByBookerId(Integer bookerId, BookingState state);

    List<BookingDto> getAllBookingsByOwnerId(Integer ownerId, BookingState state);

}
