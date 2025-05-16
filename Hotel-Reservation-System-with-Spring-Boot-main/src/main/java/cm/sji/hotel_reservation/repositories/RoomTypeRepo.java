package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepo extends JpaRepository<RoomType, Integer> {
    List<RoomType> findByHotel(Hotel hotel);
    List<RoomType> findByHotelAndNumberAvailableGreaterThan(Hotel hotel, int minAvailable);
    List<RoomType> findByPriceLessThanEqual(double maxPrice);

    @Query("SELECT rt FROM RoomType rt WHERE rt.hotel.id = :hotelId AND rt.numberAvailable > 0")
    List<RoomType> findAvailableRoomsByHotelId(Long hotelId);
}
