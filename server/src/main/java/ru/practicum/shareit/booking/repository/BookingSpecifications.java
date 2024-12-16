package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;

public class BookingSpecifications {

    public static Specification<Booking> hasBookerId(Integer userId) {
        return (root, query, cb) -> cb.equal(root.get("booker").get("id"), userId);
    }

    public static Specification<Booking> hasOwnerId(Integer userId) {
        return (root, query, cb) -> cb.equal(root.get("item").get("ownerId"), userId);
    }

    public static Specification<Booking> createSpecification(BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case CURRENT -> (root, query, cb) -> {
                query.orderBy(cb.asc(root.get("start")));
                return cb.between(cb.literal(now), root.get("start"), root.get("end"));
            };
            case FUTURE -> (root, query, cb) -> {
                query.orderBy(cb.asc(root.get("start")));
                return cb.greaterThan(root.get("end"), now);
            };
            case PAST -> (root, query, cb) -> {
                query.orderBy(cb.asc(root.get("start")));
                return cb.lessThan(root.get("end"), now);
            };
            default -> null;
        };
    }
}