package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.BookingSpecifications;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(Integer bookerId, BookingDto bookingDto) {
        Booking booking = BookingMapper.bookingDtoToBooking(bookingDto);

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("user is not found")));
        booking.setItem(itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("item is not found")));

        if (booking.getItem().getOwnerId().equals(bookerId)) {
            throw new InvalidRequestException("Item already belongs to the owner");
        }
        if (!booking.getItem().getAvailable()) {
            throw new InvalidRequestException("Item is not available");
        }
        return BookingMapper.bookingToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(Integer ownerId, Integer bookingId, Boolean approved) {
        userExists(ownerId);
        Booking booking = getBookingIfExists(bookingId);
        ownerRelevant(ownerId, booking);

        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new InvalidRequestException("Booking is already approved");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.bookingToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Integer userId, Integer bookingId) {
        userExists(userId);
        Booking booking = getBookingIfExists(bookingId);

        Integer owner = booking.getItem().getOwnerId();
        Integer booker = booking.getBooker().getId();
        if (!userId.equals(owner) && !userId.equals(booker))
            throw new InvalidRequestException("booking can be accessed only by booker and owner");

        return BookingMapper.bookingToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBookerId(Integer bookerId, BookingState state) {
        userExists(bookerId);
        List<Booking> bookings;

        switch (state) {
            case CURRENT, FUTURE, PAST -> {
                Specification<Booking> spec = BookingSpecifications.hasBookerId(bookerId);
                Specification<Booking> stateSpec = BookingSpecifications.createSpecification(state);
                spec = spec.and(stateSpec);
                bookings = bookingRepository.findAll(spec);
            }
            case REJECTED, WAITING -> bookings = bookingRepository.getAllBookingsByBookerIdAndStatus(bookerId, state);
            default -> bookings = bookingRepository.findAllByBookerId(bookerId);
        }
        return bookings.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Integer ownerId, BookingState state) {
        userExists(ownerId);

        List<Booking> bookings;

        switch (state) {
            case CURRENT, FUTURE, PAST -> {
                Specification<Booking> spec = BookingSpecifications.hasOwnerId(ownerId);
                Specification<Booking> stateSpec = BookingSpecifications.createSpecification(state);
                spec = spec.and(stateSpec);
                bookings = bookingRepository.findAll(spec);
            }
            case REJECTED, WAITING -> bookings = bookingRepository.getAllBookingsByOwnerIdAndStatus(ownerId, state);
            default -> bookings = bookingRepository.findAllByOwnerId(ownerId);
        }
        return bookings.stream().map(BookingMapper::bookingToBookingDto).collect(Collectors.toList());
    }

    private void userExists(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new ValidationException("user not found");
    }

    private Booking getBookingIfExists(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
    }

    private void ownerRelevant(Integer ownerId, Booking booking) {
        if (!booking.getItem().getOwnerId().equals(ownerId))
            throw new InvalidRequestException("approval denied: user is not the owner of the item to book");
    }

}