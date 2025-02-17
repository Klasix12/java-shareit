package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long bookerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.booker.id = :bookerId " +
            "and b.start < :time and b.end > :time")
    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndEndAfter(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartBefore(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId")
    List<Booking> findAllByOwnerId(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time between b.start and b.end")
    List<Booking> findAllByOwnerIdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime time);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time > b.end")
    List<Booking> findAllByOwnerIdAndEndAfter(Long ownerId, LocalDateTime time);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId " +
            "and :time < b.start")
    List<Booking> findAllByOwnerIdAndStartBefore(Long ownerId, LocalDateTime time);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId " +
            "and b.status = :status")
    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, BookingStatus status);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id = :itemId " +
            "and b.booker.id = :bookerId " +
            "and b.status = :status " +
            "and b.end <= :time")
    Optional<Booking> findBookingByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime time);
}
