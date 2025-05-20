package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.FAQKey;
import cm.sji.hotel_reservation.entities.Hotel;
import cm.sji.hotel_reservation.entities.Review;
import cm.sji.hotel_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, FAQKey> {
    List<Review> findByHotel(Hotel hotel);
    List<Review> findByClient(User client);
    List<Review> findByRatingGreaterThanEqual(Float rating);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.id = :hotelId")
    Double calculateAverageRatingForHotel(Integer hotelId);
}