package ru.practicum.shareit.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingStatusValidator.class)
public @interface ValidBookingStatus {
    String message() default "Invalid booking status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}