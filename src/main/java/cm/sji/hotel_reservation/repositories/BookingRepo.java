package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.BookingKey;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, BookingKey> {
    List<Booking> findByClient(User client);
    List<Booking> findByRoomType(RoomType roomType);
    List<Booking> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findByRoomTypeHotelId(Integer roomType_hotel_id);

    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate AND b.checkOutDate <= :endDate")
    List<Booking> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.clientId = :clientId")
    List<Booking> findByClientId(@Param("clientId") Integer clientId);

    @Query("SELECT b FROM Booking b WHERE b.roomType.hotel.id = :hotelId")
    List<Booking> findByHotelId(Integer hotelId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.roomType.id = :roomTypeId " +
            "AND b.status != 'CANCELLED' " +
            "AND ((b.checkInDate BETWEEN :checkIn AND :checkOut) " +
            "OR (b.checkOutDate BETWEEN :checkIn AND :checkOut) " +
            "OR (:checkIn BETWEEN b.checkInDate AND b.checkOutDate))")
    int countOverlappingBookings(Integer roomTypeId, LocalDate checkIn, LocalDate checkOut);
}
