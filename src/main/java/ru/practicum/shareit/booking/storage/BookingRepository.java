package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker, Instant time, Instant time1);

    List<Booking> findAllByBookerAndEndAfter(User booker, Instant time);

    List<Booking> findAllByBookerAndStartBefore(User booker, Instant time);

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus status);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.owner.id = :ownerId")
    List<Booking> findAllByOwner(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time between b.start and b.end")
    List<Booking> findAllByOwnerAndStartBeforeAndEndAfter(Long ownerId, Instant time);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time > b.end")
    List<Booking> findAllByOwnerAndEndAfter(Long ownerId, Instant time);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time < b.start")
    List<Booking> findALlByOwnerAndStartBefore(Long ownerId, Instant time);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.owner.id = :ownerId " +
            "and b.status = :status")
    List<Booking> findAllByOwnerAndStatus(Long ownerId, BookingStatus status);
}
