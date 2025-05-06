package cm.sji.hotel_reservation.repositories;

import cm.sji.hotel_reservation.entities.FAQKey;
import cm.sji.hotel_reservation.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepo extends JpaRepository<Review, FAQKey> {
}
