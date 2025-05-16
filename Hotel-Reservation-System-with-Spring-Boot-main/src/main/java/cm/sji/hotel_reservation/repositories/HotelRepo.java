package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocation(String location);
    List<Hotel> findByRatingGreaterThanEqual(Integer rating);
    List<Hotel> findByOwner(String owner);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Hotel> searchHotels(String keyword);
}
