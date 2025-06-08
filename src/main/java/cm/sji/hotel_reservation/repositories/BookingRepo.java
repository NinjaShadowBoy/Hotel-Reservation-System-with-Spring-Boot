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
import java.util.Arrays;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.roomType.hotel.id = :hotelId")
    List<Booking> findBookingsByHotelId(@Param("hotelId") Integer hotelId);

    List<Booking> findByClient(User client);
    List<Booking> findByRoomType(RoomType roomType);
    List<Booking> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findByRoomTypeHotelId(Integer roomType_hotel_id);

    List<Booking> findByRoomType_Hotel_Owner_Id(Integer roomTypeHotelOwnerId);

    List<Booking> findByClient_Id(Integer clientId);

    Booking findByRefundId(String refundId);
}
