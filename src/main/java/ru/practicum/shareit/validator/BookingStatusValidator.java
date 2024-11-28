package ru.practicum.shareit.validator;

import ru.practicum.shareit.booking.model.BookingStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class BookingStatusValidator implements ConstraintValidator<ValidBookingStatus, BookingStatus> {

    @Override
    public boolean isValid(BookingStatus value, ConstraintValidatorContext context) {
        return Arrays.asList(BookingStatus.values()).contains(value);
    }
}