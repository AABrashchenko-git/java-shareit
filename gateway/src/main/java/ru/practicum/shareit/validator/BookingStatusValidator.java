package ru.practicum.shareit.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingStatus;

import java.util.Arrays;

public class BookingStatusValidator implements ConstraintValidator<ValidBookingStatus, BookingStatus> {

    @Override
    public boolean isValid(BookingStatus value, ConstraintValidatorContext context) {
        return Arrays.asList(BookingStatus.values()).contains(value);
    }
}