package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByOwner(String owner);
    List<Hotel> findByNameContainingIgnoreCase(String name);
    List<Hotel> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT h FROM Hotel h WHERE h.rating >= :minRating")
    List<Hotel> findByRatingGreaterThanEqual(Double minRating);

    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(h.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Hotel> searchHotels(String keyword);
}
