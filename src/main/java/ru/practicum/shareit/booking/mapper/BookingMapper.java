package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;

@UtilityClass
public class BookingMapper {
    public Booking bookingDtoToBooking(BookingDto bookingDto) {
        return Booking.builder().id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                //.itemId(bookingDto.getItemId())
                //.bookerId(bookingDto.getBookerId())
                .status(bookingDto.getStatus() == null ? null : bookingDto.getStatus())
                .build();
    }

    public BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }
}
