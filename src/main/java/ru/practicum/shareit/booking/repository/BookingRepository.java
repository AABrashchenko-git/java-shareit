package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT b FROM Booking as b JOIN b.booker AS u " +
            "WHERE u.id = :bookerId AND b.status = :state ORDER BY b.start ASC")
    List<Booking> getAllBookingsByBookerIdAndStatus(@Param("bookerId") Integer bookerId, @Param("state") BookingState state);

    @Query("SELECT b FROM Booking as b JOIN b.item AS i " +
            "WHERE i.ownerId = :ownerId AND b.status = :state ORDER BY b.start ASC")
    List<Booking> getAllBookingsByOwnerIdAndStatus(@Param("ownerId") Integer ownerId, @Param("state") BookingState state);

    @Query("SELECT b FROM Booking as b JOIN b.booker AS u " +
            "WHERE u.id = :bookerId ORDER BY b.start ASC")
    List<Booking> findAllByBookerId(@Param("bookerId") Integer bookerId);

    @Query("SELECT b FROM Booking as b JOIN b.item AS i " +
            "WHERE i.ownerId = :ownerId ORDER BY b.start ASC")
    List<Booking> findAllByOwnerId(@Param("ownerId") Integer ownerId);

    @Query("SELECT b FROM Booking as b JOIN b.item AS i WHERE i.ownerId = :ownerId")
    List<Booking> findAllByItemOwnerId(@Param("ownerId") Integer ownerId);

    List<Booking> findAllByItemIdAndBookerId(Integer itemId, Integer bookerId);

}
