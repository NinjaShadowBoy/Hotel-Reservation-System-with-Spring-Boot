package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Booking;
import cm.sji.hotel_reservation.entities.BookingKey;
import cm.sji.hotel_reservation.entities.RoomType;
import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, BookingKey> {
    List<Booking> findByClient(User client);
    List<Booking> findByRoom_Type(RoomType roomType);
    List<Booking> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Booking b WHERE b.room_Type.hotel.id = :hotelId")
    List<Booking> findByHotelId(Long hotelId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room_Type.id = :roomTypeId AND b.date = :bookingDate")
    long countBookingsByRoomTypeAndDate(Integer roomTypeId, LocalDate bookingDate);
}