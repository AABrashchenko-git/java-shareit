package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@UtilityClass
public class BookingMapper {
    public Booking bookingDtoToBooking(BookingDto bookingDto) {
        return Booking.builder().id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus() == null ? null : bookingDto.getStatus())
                .build();
    }

    public BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.itemToItemDto(booking.getItem()))
                .booker(UserMapper.userToUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }
}
